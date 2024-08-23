'use client'
import { useEffect, useState } from 'react'
import MyPageInfoComponent from './components/mypageinfo'
import { getMyPosts, myPostAllResponse } from '@/app/_api/myPage'
import PostCard from './components/PostCard' // 이름을 PostCard로 변경
import './mypage.scss'

export default function MyPage() {
    const [myPosts, setMyPosts] = useState<myPostAllResponse[]>([])

    useEffect(() => {
        async function fetchPosts() {
            try {
                const posts = await getMyPosts()
                // 이미지 ID가 큰 순서로 정렬
                const sortedPosts = posts.sort((a, b) => b.imageId - a.imageId)
                setMyPosts(sortedPosts)
            } catch (error) {
                console.error('Failed to fetch posts:', error)
            }
        }
        fetchPosts()
    }, [])

    return (
        <div className='mypage-container'>
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
                            key={post.postId} // 수정: postId를 key로 사용
                            imageId={post.imageId}
                            postContent={post.postContent}
                            likeCount={post.likeCount}
                            postId={post.postId} // 추가: postId 전달
                            hashtags={post.hashtags} // 추가: 해시태그 전달
                        />
                    ))
                ) : (
                    <p>아직 업로드된 글이 없습니다.</p>
                )}
            </div>
        </div>
    )
}
