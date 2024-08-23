'use client'
import { useEffect, useState, useCallback } from 'react'
import Link from 'next/link' // next/link를 import
import { getAllPostPreviews, postPreviewContent } from '../../_api/fetchStyle'
import StylePreview from '@/components/post-preview'
import './gallery.scss'
import HeaderSearch from '@/components/search-bar'
import { useIntersectionObserver } from './useIntersectionObserver'
import { myInfoAllFunction, myInfoAllResponse } from '@/app/_api/myPage'
import { useRouter } from 'next/navigation'

export default function Gallery() {
    const [styles, setStyles] = useState<postPreviewContent[]>([])
    const [page, setPage] = useState<number>(0)
    const [hasMore, setHasMore] = useState<boolean>(true)
    const [search, setSearch] = useState<string>('')
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [redirectToSignup, setRedirectToSignup] = useState<boolean>(false) // 회원가입 페이지로 리다이렉트할지 여부
    const router = useRouter()
    const fetchData = useCallback(async () => {
        try {
            const request = search.startsWith('#')
                ? { hashtags: search.slice(1), page }
                : { postContent: search, page }

            const response = await getAllPostPreviews(request)
            setStyles(prevStyles => [...prevStyles, ...response.content])
            setHasMore(
                response.content.length > 0 && response.totalPages > page + 1,
            )
        } catch (error) {
            console.error('Failed to fetch data:', error)
        }
    }, [search, page])

    useEffect(() => {
        fetchData()
    }, [fetchData])

    const handleSearch = (search: string) => {
        setSearch(search)
        setPage(0)
        setStyles([]) // 새로운 검색어로 검색할 때 기존 데이터를 초기화
    }

    const lastElementRef = useIntersectionObserver(
        () => setPage(prev => prev + 1),
        hasMore,
    )

    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const data = await myInfoAllFunction()
                setUserInfo(data)

                // Gender가 null일 경우 회원가입 페이지로 리다이렉트
                if (data.gender === null) {
                    setRedirectToSignup(true) // 리다이렉트 상태 설정
                }
            } catch (error) {
                console.error('Failed to fetch user info:', error)
            }
        }

        fetchUserInfo()
    }, [])

    // 리다이렉트 처리
    if (redirectToSignup) {
        return router.push('/signup')
    }

    return (
        <div className='container'>
            <HeaderSearch onSearch={handleSearch} />
            <section className='gallery'>
                {styles?.map((item, index) => (
                    <StylePreview
                        className='gallery-item'
                        content={item}
                        key={index}
                    />
                ))}
                <div ref={lastElementRef}></div>{' '}
                {/* 마지막 요소에 대한 ref 추가 */}
            </section>
        </div>
    )
}
