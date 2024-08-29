'use client'

import React, { useState, useEffect, useCallback } from 'react'
import {
    fetchCategories,
    CategoryEntity,
    fetchPostsByColor,
} from '@/app/_api/category'
import {
    fetchPostsByCategory,
    fetchPostsByCategoryAndColor,
} from '@/app/_api/category'
import StylePreview from '@/components/post-preview'
import HeaderSearch from '@/components/search-bar'
import { useIntersectionObserver } from './useIntersectionObserver'
import { myInfoAllFunction, myInfoAllResponse } from '@/app/_api/myPage'
import { useRouter } from 'next/navigation'
import Image from 'next/image'
import CategoryList from '@/components/CategoryList'
import Masonry from 'react-masonry-css'
import galleryStyles from './gallery.module.scss'
import { getAllPostPreviews, postPreviewContent } from '@/app/_api/fetchStyle'

export default function Gallery() {
    const [styles, setStyles] = useState<postPreviewContent[]>([])
    const [categories, setCategories] = useState<CategoryEntity[]>([])
    const [page, setPage] = useState<number>(0)
    const [hasMore, setHasMore] = useState<boolean>(true)
    const [search, setSearch] = useState<string>('')
    const [category, setCategory] = useState<string | null>(null)
    const [color, setColor] = useState<number[] | null>(null)
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [redirectToSignup, setRedirectToSignup] = useState<boolean>(false)
    const router = useRouter()

    useEffect(() => {
        // 카테고리 데이터를 가져오는 로직 추가
        const fetchCategoriesData = async () => {
            try {
                const categoriesData = await fetchCategories()
                setCategories(categoriesData)
            } catch (error) {
                console.error('Failed to fetch categories', error)
            }
        }

        fetchCategoriesData()
    }, [])

    const fetchData = useCallback(async () => {
        try {
            let response

            if (category && color) {
                // 카테고리와 색상으로 필터링
                if (category === '전체') {
                    response = await fetchPostsByColor(color, page, 10)
                } else {
                    response = await fetchPostsByCategoryAndColor(
                        { category, rgbColor: color },
                        page,
                        10,
                    )
                }
            } else if (category) {
                const categoryId =
                    categories.find(cat => cat.categoryContent === category)
                        ?.categoryId || 0
                response = await fetchPostsByCategory(categoryId, page, 10)
            } else {
                const request = search.startsWith('#')
                    ? { hashtags: search.slice(1), page }
                    : { postContent: search, page }
                response = await getAllPostPreviews(request)
            }

            setStyles(prevStyles => [...prevStyles, ...response.content])
            setHasMore(
                response.content.length > 0 && page < response.totalPages,
            )
        } catch (error) {
            console.error('Failed to fetch data:', error)
        }
    }, [search, page, category, color, categories])

    useEffect(() => {
        fetchData()
    }, [fetchData])

    const handleSearch = (search: string) => {
        setSearch(search)
        setPage(0)
        setStyles([])
        setCategory(null)
        setColor(null)
    }

    const handleCategorySelect = (selectedCategory: string) => {
        setCategory(selectedCategory)
        setPage(0)
        setStyles([])
    }

    const handleCategoryAndColorSelect = (
        selectedCategory: string,
        selectedColor: number[],
    ) => {
        if (selectedCategory === '전체') {
            setCategory('전체')
            setColor(selectedColor)
            console.log(selectedColor)
        } else {
            setCategory(selectedCategory)
            setColor(selectedColor)
        }
        setPage(0)
        setStyles([])
    }

    const lastElementRef = useIntersectionObserver(() => {
        if (hasMore) {
            setPage(prev => prev + 1)
        }
    }, hasMore)

    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const data = await myInfoAllFunction()
                setUserInfo(data)

                if (data.gender === null) {
                    setRedirectToSignup(true)
                }
            } catch (error) {
                console.error('Failed to fetch user info:', error)
            }
        }

        fetchUserInfo()
    }, [])

    if (redirectToSignup) {
        return router.push('/signup')
    }

    const breakpointColumnsObj = {
        default: 2,
        1100: 2,
        700: 2,
    }

    return (
        <div className={galleryStyles.container}>
            <div className={galleryStyles.logoWrapper}>
                <Image
                    src='/images/LOOKUSlogo.png'
                    alt='LOOK:US Logo'
                    width={171}
                    height={36}
                    priority={true}
                />
            </div>
            <HeaderSearch onSearch={handleSearch} />
            <CategoryList
                categories={categories}
                onSelectCategory={handleCategorySelect}
                onSelectCategoryAndColor={handleCategoryAndColorSelect}
            />
            <Masonry
                breakpointCols={breakpointColumnsObj}
                className={galleryStyles.masonryGrid}
                columnClassName={galleryStyles.masonryGridColumn}
            >
                {styles.map((item, index) => (
                    <StylePreview
                        className={galleryStyles.galleryItem}
                        content={item}
                        key={index}
                    />
                ))}
            </Masonry>
            <div ref={lastElementRef}></div>
        </div>
    )
}
