'use client'
import { useEffect, useState, useCallback } from 'react'
import { getAllPostPreviews, postPreviewContent } from '../../_api/fetchStyle'
import StylePreview from '@/components/post-preview'
import './gallery.scss'
import HeaderSearch from '@/components/search-bar'
import { useIntersectionObserver } from './useIntersectionObserver' // 방금 만든 훅 import

export default function Gallery() {
    const [styles, setStyles] = useState<postPreviewContent[]>([])
    const [page, setPage] = useState<number>(0)
    const [hasMore, setHasMore] = useState<boolean>(true)
    const [search, setSearch] = useState<string>('')

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
