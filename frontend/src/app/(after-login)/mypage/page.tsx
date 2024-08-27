'use client'

import { useEffect, useState } from 'react'
import MyPageInfoComponent from './components/mypageinfo'
import {
    getMyPosts,
    myInfoAllFunction,
    myInfoAllResponse,
    myPostAllResponse,
} from '@/app/_api/myPage'
import PostCard from './components/PostCard'
import './mypage.scss'
import { useRouter } from 'next/navigation'
import { parseCookies } from 'nookies' // 쿠키 파싱을 위해 nookies 라이브러리 사용
import Modal from '@/components/modal-accesscontrol'

export default function MyPage() {
    const [myPosts, setMyPosts] = useState<myPostAllResponse[]>([])
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [showModal, setShowModal] = useState<boolean>(false)

    const router = useRouter()

    useEffect(() => {
        const cookies = parseCookies() // 쿠키 파싱
        // const token = cookies.token

        if (!cookies) {
            setShowModal(true)
            return
        }

        async function fetchData() {
            try {
                const posts = await getMyPosts()
                const data = await myInfoAllFunction()
                setUserInfo(data)

                // Gender가 null일 경우 회원가입 페이지로 리다이렉트
                if (data.gender === null) {
                    router.push('/signup')
                }

                // 이미지 ID가 큰 순서로 정렬
                const sortedPosts = posts.sort((a, b) => b.imageId - a.imageId)
                setMyPosts(sortedPosts)
            } catch (error) {
                console.error('Failed to fetch posts:', error)
            }
        }

        fetchData()
    }, [router])

    const handleCloseModal = () => {
        setShowModal(false)
        router.push('/posts')
    }

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
                            key={post.postId}
                            imageId={post.imageId}
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

            <Modal show={showModal} onClose={handleCloseModal} />
        </div>
    )
}
