'use client'
import { useCallback, useEffect, useState } from 'react'
import { getAllPostPreviews, postPreviewContent } from '../../_api/fetchStyle'
import StylePreview from '@/components/post-preview'
import './gallery.scss'
import HeaderSearch from '@/components/search-bar'
import { useIntersectionObserver } from './useIntersectionObserver'
import { myInfoAllFunction, myInfoAllResponse } from '@/app/_api/myPage'
import { useRouter } from 'next/navigation'

// 프론트엔드에서 관리할 페이지네이션 상태와 데이터 타입 정의
interface PostPreviewResponse {
    content: postPreviewContent[]
    totalElements: number
    totalPages: number
    size: number
}

export default function Gallery() {
    const [styles, setStyles] = useState<postPreviewContent[]>([])
    const [page, setPage] = useState<number>(0)
    const [hasMore, setHasMore] = useState<boolean>(true)
    const [search, setSearch] = useState<string>('')
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [redirectToSignup, setRedirectToSignup] = useState<boolean>(false) // 회원가입 페이지로 리다이렉트할지 여부
    const [totalElements, setTotalElements] = useState<number>(0) // 총 게시물 수를 관리하는 상태
    const [size, setSize] = useState<number>(10) // 페이지 크기 상태 추가
    const router = useRouter()
    const fetchData = useCallback(async () => {
        try {
            const request = search.startsWith('#')
                ? { hashtags: search.slice(1), page }
                : { postContent: search, page }

            const response = await getAllPostPreviews(request)
            setStyles(prevStyles => [...prevStyles, ...response.content])
            setTotalElements(response.totalElements) // 총 게시물 수 업데이트
            setSize(response.size) // 페이지 크기 업데이트

            // hasMore 상태를 totalElements와 현재 페이지를 기반으로 업데이트
            const currentPageSize = response.content.length // 현재 페이지의 데이터 수
            setHasMore(
                currentPageSize > 0 &&
                    (page + 1) * response.size < response.totalElements, // 서버에서 받은 페이지 크기 사용
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

    const lastElementRef = useIntersectionObserver(() => {
        if (hasMore) {
            // 더 가져올 데이터가 있을 때만 페이지 증가
            setPage(prev => prev + 1)
        }
    }, hasMore)

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
