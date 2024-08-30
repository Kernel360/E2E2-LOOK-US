'use client'

import React, { useState, useEffect, useCallback } from 'react'
import { fetchCategories, CategoryEntity } from '@/app/_api/category'
import {
    fetchPostsByCategory,
    fetchPostsByCategoryAndColor,
} from '@/app/_api/category'
import StylePreview from '@/components/post-preview'
import HeaderSearch from '@/components/search-bar'
import { myInfoAllFunction, myInfoAllResponse } from '@/app/_api/myPage'
import { useRouter } from 'next/navigation'
import Image from 'next/image'
import CategoryList from '@/components/CategoryList'
import Masonry from 'react-masonry-css'
import galleryStyles from './gallery.module.scss'
import { getAllPostPreviews, postPreviewContent } from '@/app/_api/fetchStyle'
import Link from 'next/link'

export default function Gallery() {
    const [styles, setStyles] = useState<postPreviewContent[]>([])
    const [categories, setCategories] = useState<CategoryEntity[]>([])
    const [page, setPage] = useState<number>(0)
    const [hasMore, setHasMore] = useState<boolean>(true)
    const [search, setSearch] = useState<string>('')
    const [categoryId, setCategoryId] = useState<number | null>(null) // string -> number로 변경
    const [color, setColor] = useState<number[] | null>(null)
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [redirectToSignup, setRedirectToSignup] = useState<boolean>(false)
    const router = useRouter()
    const [isFetching, setIsFetching] = useState<boolean>(false)
    const [totalPages, setTotalPages] = useState<number>(0)

    useEffect(() => {
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

    const fetchData = async (page: number) => {
        let response

        const isColorDefault =
            !color || (color[0] === 255 && color[1] === 255 && color[2] === 255)

        if (categoryId && !isColorDefault) {
            console.log('categoryId와 color 모두 선택됨')
            response = await fetchPostsByCategoryAndColor(
                { categoryId, rgbColor: color },
                page,
                10,
            )
        } else if (categoryId) {
            console.log('categoryId만 선택됨')
            response = await fetchPostsByCategory(categoryId, page, 10)
        } else {
            const request = search.startsWith('#')
                ? { hashtags: search.slice(1), page }
                : { postContent: search, page }
            response = await getAllPostPreviews(request)
        }

        return response
    }

    const loadAllPages = async () => {
        try {
            const firstPageResponse = await fetchData(0)
            const totalPages = firstPageResponse.totalPages
            setTotalPages(totalPages)

            let allPosts: postPreviewContent[] = []
            for (let page = 0; page < totalPages; page++) {
                const pageResponse = await fetchData(page)
                allPosts = [...allPosts, ...pageResponse.content]
            }

            setStyles(allPosts)
        } catch (error) {
            console.error('Failed to fetch data:', error)
        }
    }

    useEffect(() => {
        loadAllPages()
    }, [search, categoryId, color])

    const handleSearch = (search: string) => {
        setSearch(search)
        setStyles([])
        setCategoryId(null)
        setColor(null)
    }

    const handleCategorySelect = (selectedCategoryId: number) => {
        // string -> number로 변경
        setCategoryId(selectedCategoryId)
        setStyles([])
    }

    const handleCategoryAndColorSelect = (
        selectedCategoryId: number, // string -> number로 변경
        selectedColor: number[],
    ) => {
        setCategoryId(selectedCategoryId)
        setColor(selectedColor)
        setStyles([])
    }

    const handleColorChange = (rgbColor: number[]) => {
        setColor(rgbColor)
        setStyles([])
    }

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
                <div className={galleryStyles.buttonWrapper}>
                    <Link href='/posts/new'>
                        <Image
                            src='/images/postAddBtn.png'
                            alt='Post Add Button'
                            width={36}
                            height={36}
                            className={galleryStyles.iconButton}
                        />
                    </Link>
                    <Link href='/mypage'>
                        <Image
                            src='/images/myPageBtn.png'
                            alt='My Page Button'
                            width={36}
                            height={36}
                            className={galleryStyles.iconButton}
                        />
                    </Link>
                </div>
            </div>
            <HeaderSearch onSearch={handleSearch} />
            <CategoryList
                categories={categories}
                onSelectCategory={handleCategorySelect}
                onSelectCategoryAndColor={handleCategoryAndColorSelect}
                onColorChange={handleColorChange}
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
        </div>
    )
}
