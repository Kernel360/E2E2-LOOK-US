'use client'

import { getPost, likePost } from '@/app/_api/post';
import { useEffect, useState } from 'react';
import Image from 'next/image';
import styles from './Post.module.scss';
import { API_PUBLIC_URL } from '@/app/_common/constants';
import { useRouter } from 'next/navigation';
import { follow, FollowRequest } from '@/app/_api/follow';
import { AiFillHeart, AiOutlineHeart } from 'react-icons/ai';

type Props = {
    params: { post_id: number };
    searchParams: { [key: string]: string | string[] | undefined };
};

export default function Page({ params, searchParams }: Props) {
    const [post, setPost] = useState<any>(null);
    const [isFollowing, setIsFollowing] = useState<boolean>(false);
    const [likeCount, setLikeCount] = useState<number>(0);
    const [liked, setLiked] = useState<boolean>(false);
    const router = useRouter();

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const post = await getPost(params.post_id);
                console.log('API Response:', post); // API 응답 확인
                setPost(post);
                setLikeCount(post.likeCount);
                setLiked(post.likeStatus); // API에서 받은 likeStatus를 설정
            } catch (error) {
                console.error('Failed to fetch post data:', error);
            }
        };
        fetchPost();
    }, [params.post_id]);

    const handleLikeClick = async () => {
        try {
            const isLiked = await likePost(params.post_id);
            setLiked(isLiked);
            setLikeCount(prevCount => (isLiked ? prevCount + 1 : prevCount - 1));
        } catch (error) {
            console.error('Failed to update like status:', error);
        }
    };

    const handleFollowClick = async () => {
        try {
            const request: FollowRequest = {
                nickname: post.nickname,
                followStatus: isFollowing ? 0 : 1,
            };
            await follow(request);
            setIsFollowing(prev => !prev);
        } catch (error) {
            console.error('Failed to update follow status:', error);
        }
    };

    if (!post) return <div>Loading...</div>;

    return (
        <div className={styles.postContainer}>
            <div className={styles.imageContainer}>
                <Image
                    src={`${API_PUBLIC_URL}/image/${post.imageId}`}
                    alt='style'
                    unoptimized={true}
                    priority={true}
                    width={600}
                    height={800}
                    sizes='100%'
                />
            </div>
            <div className={styles.header}>
                <div className={styles.profileInfo}>
                    <div className={styles.profilePic}>
                        <img
                            src='https://private-user-images.githubusercontent.com/60287070/356863617-dd8f7943-e1bc-4003-bf45-78b5346b77e5.png'
                            alt='profile'
                        />
                    </div>
                    <span className={styles.username}>{post.nickname}</span>
                </div>
                
                <div className={styles.actions}>
                    <button className={styles.likeButton} onClick={handleLikeClick}>
                        {liked ? (
                            <AiFillHeart className={styles.heartIcon} />
                        ) : (
                            <AiOutlineHeart className={styles.heartIcon} />
                        )}
                    </button>
                    <span className={styles.likeCount}>{likeCount}</span>
                    <button
                        className={
                            isFollowing
                                ? styles.followingButton
                                : styles.followButton
                        }
                        onClick={handleFollowClick}
                    >
                        {isFollowing ? '팔로잉' : '팔로우'}
                    </button>
                </div>
            </div>
            <div className={styles.content}>{post.postContent}</div>
            <div className={styles.hashtags}>
                {post.hashtagContents.map((hashtag: string) => (
                    <span
                        key={hashtag}
                        className={styles.hashtag}
                        onClick={() => router.push(`/search?hashtags=${encodeURIComponent(hashtag)}`)}
                    >
                        #{hashtag}
                    </span>
                ))}
            </div>
        </div>
    );
}
