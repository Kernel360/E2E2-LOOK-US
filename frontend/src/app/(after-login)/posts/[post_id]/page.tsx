'use client'

import { getPost, likePost } from '@/app/_api/post'
import { useEffect, useState } from 'react'
import Image from 'next/image'
import styles from './Post.module.scss'
import { API_PUBLIC_URL } from '@/app/_common/constants'
import { useRouter } from 'next/navigation'
import { follow, FollowRequest } from '@/app/_api/follow'
import { AiFillHeart, AiOutlineHeart } from 'react-icons/ai'
import { FaRegBookmark, FaBookmark } from 'react-icons/fa'

type Props = {
    params: { post_id: number }
    searchParams: { [key: string]: string | string[] | undefined }
}

export default function Page({ params, searchParams }: Props) {
    const [post, setPost] = useState<any>(null)
    const [isFollowing, setIsFollowing] = useState<boolean>(false)
    const [likeCount, setLikeCount] = useState<number>(0)
    const [liked, setLiked] = useState<boolean>(false)
    const [scraped, setScraped] = useState<boolean>(false) // 스크랩 여부 상태 추가
    const router = useRouter()

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const post = await getPost(params.post_id)
                console.log('API Response:', post) // API 응답 확인
                setPost(post)
                setLikeCount(post.likeCount)
                setLiked(post.likeStatus) // API에서 받은 likeStatus를 설정
            } catch (error) {
                console.error('Failed to fetch post data:', error)
            }
        }
        fetchPost()
    }, [params.post_id])

    const handleLikeClick = async () => {
        try {
            const isLiked = await likePost(params.post_id)
            setLiked(isLiked)
            setLikeCount(prevCount => (isLiked ? prevCount + 1 : prevCount - 1))
        } catch (error) {
            console.error('Failed to update like status:', error)
        }
    }

    const handleFollowClick = async () => {
        try {
            const request: FollowRequest = {
                nickname: post.nickname,
                followStatus: isFollowing ? 0 : 1,
            }
            await follow(request)
            setIsFollowing(prev => !prev)
        } catch (error) {
            console.error('Failed to update follow status:', error)
        }
    }

    const handleScrapClick = () => {
        setScraped(prev => !prev)
    }

    if (!post) return <div>Loading...</div>

    return (
        <div className={styles.postContainer}>
            <div className={styles.imageContainer}>
                <Image
                    src={`${API_PUBLIC_URL}/image/${post.imageLocationId}`}
                    alt='style'
                    unoptimized={true}
                    priority={true}
                    width={600}
                    height={800}
                    sizes='100%'
                />
            </div>

            <div className={styles.actions}>
                <div className={styles.actionItem}>
                    <button
                        className={styles.likeButton}
                        onClick={handleLikeClick}
                    >
                        {liked ? (
                            <AiFillHeart className={styles.heartIcon} />
                        ) : (
                            <AiOutlineHeart className={styles.heartIcon} />
                        )}
                    </button>
                    <span className={styles.count}>{likeCount}</span>
                </div>

                <div className={styles.actionItem}>
                    <button
                        className={styles.scrapButton}
                        onClick={handleScrapClick}
                    >
                        {scraped ? (
                            <FaBookmark className={styles.scrapIcon} />
                        ) : (
                            <FaRegBookmark className={styles.scrapIcon} />
                        )}
                    </button>
                </div>

                <span className={styles.viewsCount}>{post.hits}번 조회</span>
            </div>

            <div className={styles.contentBox}>
                <div className={styles.contentTitle}>게시글</div>

                <div className={styles.content}>{post.postContent}</div>
            </div>

            <div className={styles.categoriesBox}>
                <div className={styles.categoryTitle}>카테고리</div>
                <div className={styles.categoryList}>
                    {post.categories.map((category: string) => (
                        <span key={category} className={styles.category}>
                            {category}
                        </span>
                    ))}
                </div>
            </div>

            <div className={styles.hashtagsBox}>
                <div className={styles.hashtagTitle}>해시태그</div>
                <div className={styles.hashtagList}>
                    {post.hashtagContents.length > 0 ? (
                        post.hashtagContents.map((hashtag: string) => (
                            <span
                                key={hashtag}
                                className={styles.hashtag}
                                onClick={() =>
                                    router.push(
                                        `/search?hashtags=${encodeURIComponent(hashtag)}`,
                                    )
                                }
                            >
                                #{hashtag}
                            </span>
                        ))
                    ) : (
                        <div className={styles.hashtagPlaceholder}>-</div>
                    )}
                </div>
            </div>

            <div className={styles.profileDetailsBox}>
                <div className={styles.profileDetails}>
                    <div className={styles.profilePic}>
                        <Image
                            src={`${API_PUBLIC_URL}/image/${post.profileImageLocationId}`}
                            alt='profile'
                            width={50} // 고정된 너비
                            height={50} // 고정된 높이
                            className={styles.profileImage}
                        />
                    </div>
                    <div className={styles.profileInfo}>
                        <span className={styles.username}>{post.nickname}</span>
                        <div>
                            <span className={styles.stats}>팔로워 0명</span>
                            <span className={styles.stats}>팔로잉 0명</span>
                        </div>
                    </div>
                </div>

                <div className={styles.followButtonContainer}>
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
        </div>
    )
}
