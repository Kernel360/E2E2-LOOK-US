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
import Link from 'next/link' // Link 컴포넌트 import
import { myInfoAllFunction, myInfoAllResponse } from '@/app/_api/myPage'

type Props = {
    params: { post_id: number }
    searchParams: { [key: string]: string | string[] | undefined }
}

export default function Page({ params, searchParams }: Props) {
    const [post, setPost] = useState<any>(null)
    const [isFollowing, setIsFollowing] = useState<boolean>(false)
    const [likeCount, setLikeCount] = useState<number>(0)
    const [liked, setLiked] = useState<boolean>(false)
    const [scraped, setScraped] = useState<boolean>(false)
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null) // 유저 정보
    const [showModal, setShowModal] = useState<boolean>(false) // 모달창 상태

    const router = useRouter()
    // 유저 정보 가져오기
    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const data = await myInfoAllFunction()
                setUserInfo(data)
            } catch (error) {
                console.error('Failed to fetch user info:', error)
            }
        }

        fetchUserInfo()
    }, [])

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const post = await getPost(params.post_id)
                console.log('API Response:', post)
                setPost(post)
                setLikeCount(post.likeCount)
                setLiked(post.likeStatus)
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
    const handleModalOpen = () => {
        setShowModal(true)
    }

    const handleModalClose = () => {
        setShowModal(false)
    }
    if (!post) return <div>Loading...</div>

    return (
        <div className={styles.postContainer}>
            <div className={styles.logoContainer}>
                <Link href='/posts'>
                    {/* 메인페이지로 이동하도록 Link 추가 */}
                    <Image
                        src='/images/LOOKUSlogo.png'
                        alt='LOOK:US Logo'
                        width={171}
                        height={36}
                        priority={true}
                    />
                </Link>
            </div>

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
                                        `/search?hashtags=${encodeURIComponent(
                                            hashtag,
                                        )}`,
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
                            width={50}
                            height={50}
                            className={styles.profileImage}
                        />
                    </div>
                    <div className={styles.profileInfo}>
                        <span className={styles.username}>{post.nickname}</span>
                        <div className={styles.statsContainer}>
                            <span className={styles.stats}>팔로워 0명</span>
                            <span className={styles.stats}>팔로잉 0명</span>
                        </div>
                    </div>
                </div>

                {/* 팔로우 버튼: 본인 포스트가 아니면 보여줌 */}
                {userInfo?.nickname !== post.nickname && (
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
                )}

                {/* 내 글일 경우 동그라미 버튼 표시 */}
                {userInfo?.nickname === post.nickname && (
                    <div
                        className={styles.optionsButton}
                        onClick={handleModalOpen}
                    >
                        &#x22EE; {/* 세 개의 점 (vertival ellipsis) */}
                    </div>
                )}
            </div>
            {/* 모달창 */}
            {showModal && (
                <div className={styles.modalBackdrop}>
                    <div className={styles.modalContainer}>
                        <div className={styles.modalButtonContainer}>
                            <button className={styles.modalButton}>
                                내 게시글 편집하기
                            </button>
                            <button className={styles.modalButton}>
                                내 게시글 삭제하기
                            </button>
                        </div>
                        <button
                            className={styles.modalCancel}
                            onClick={handleModalClose}
                        >
                            취소
                        </button>
                    </div>
                </div>
            )}
        </div>
    )
}
