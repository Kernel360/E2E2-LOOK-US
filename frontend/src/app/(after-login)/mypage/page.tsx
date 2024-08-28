'use client'

import { useAuth } from '@/app/_api/useAuth'
import MyPageInfoComponent from './components/mypageinfo'
import PostCard from './components/PostCard'
import './mypage.scss'
import Modal from '@/components/modal-accesscontrol'
import { useEffect, useState } from 'react'
import { getMyPosts, myPostAllResponse } from '@/app/_api/myPage'

export default function MyPage() {
    const { userInfo, showModal, handleCloseModal } = useAuth()
    const [myPosts, setMyPosts] = useState<myPostAllResponse[]>([])

    useEffect(() => {
        if (!userInfo) return

        async function fetchData() {
            try {
                const posts = await getMyPosts()

                // 이미지 ID가 큰 순서로 정렬
                const sortedPosts = posts.sort(
                    (a, b) => b.imageLocationId - a.imageLocationId,
                )
                setMyPosts(sortedPosts)
            } catch (error) {
                console.error('Failed to fetch posts:', error)
            }
        }

        fetchData()
    }, [userInfo])

    return (
        <div className='mypage-container'>
            <Modal show={showModal} onClose={handleCloseModal} />
            {!showModal && userInfo && (
                <>
                    <div className='mypage-header'>
                        <MyPageInfoComponent />
                    </div>

                    <div className='mypage-tabs'>
                        <button className='active'>내 글</button>
                        <button>답글</button>
                        <button>리포스트</button>
                    </div>

                    <div className='mypage-posts'>
                        {myPosts.length > 0 ? (
                            myPosts.map(post => (
                                <PostCard
                                    key={post.postId}
                                    imageLocationId={post.imageLocationId}
                                    postContent={post.postContent}
                                    likeCount={post.likeCount}
                                    postId={post.postId}
                                    hashtags={post.hashtags}
                                />
                            ))
                        ) : (
                            <p>아직 업로드된 글이 없습니다.</p>
                        )}
                    </div>
                </>
            )}
        </div>
    )
}
