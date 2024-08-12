'use client'

import { useEffect, useState } from 'react'
import { getAllPostPreviews, postPreviewContent } from '../../_api/fetchStyle'
import StylePreview from '@/components/post-preview'
import './gallery.scss'
import HeaderSearch from '@/components/search-bar'

export default function Gallery() {
    const [styles, setStyles] = useState<postPreviewContent[]>([])
    const [search, setSearch] = useState<string>('')

    useEffect(() => {
        const fetchData = async () => {
            try {
                const request = search.startsWith('#')
                    ? { hashtags: search.slice(1) } // 해시태그로 검색
                    : { postContent: search } // 일반 텍스트로 검색

                const response = await getAllPostPreviews(request)
                setStyles(response.content)
            } catch (error) {
                console.error('Failed to fetch data:', error)
            }
        }
        fetchData()
    }, [search])

    const handleSearch = (search: string) => {
        setSearch(search)
    }

    return (
        <div className=''>
            <HeaderSearch onSearch={handleSearch} />
            <section className='gallery'>
                {styles?.map((item, index) => (
                    <StylePreview
                        className='gallery-item'
                        content={item}
                        key={index}
                    />
                ))}
            </section>
        </div>
    )
}
