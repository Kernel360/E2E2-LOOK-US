// src/app/_api/category.ts

import { API_PUBLIC_URL } from '../_common/constants'

export interface PostDtoResponse {
    postId: number
    imageLocationId: number
    nickname: string
    hashtags: string[]
    categories: string[]
    likeCount: number
    hits: number
    createdAt: string
}

export interface PageResponse<T> {
    content: T[]
    totalPages: number
    totalElements: number
    first: boolean
    last: boolean
    size: number
    number: number
    sort: {
        empty: boolean
        sorted: boolean
        unsorted: boolean
    }
    numberOfElements: number
    pageable: {
        pageNumber: number
        pageSize: number
        sort: {
            empty: boolean
            sorted: boolean
            unsorted: boolean
        }
        offset: number
        paged: boolean
        unpaged: boolean
    }
    empty: boolean
}

// 인기 있는 컬러 응답 형식 정의
export interface PopularColor {
    name: string
    r: number
    g: number
    b: number
}
export interface CategoryEntity {
    categoryId: number
    categoryContent: string
}

export interface CategoryAndColorSearchCondition {
    category: string
    rgbColor: number[]
}

// Fetch all categories
export const fetchCategories = async (): Promise<CategoryEntity[]> => {
    const response = await fetch(`${API_PUBLIC_URL}/posts/categoryAll`)
    if (!response.ok) {
        throw new Error('Failed to fetch categories')
    }
    return response.json()
}

// Fetch posts by category ID
export const fetchPostsByCategory = async (
    categoryId: number,
    page: number,
    size: number,
): Promise<PageResponse<PostDtoResponse>> => {
    const response = await fetch(
        `${API_PUBLIC_URL}/posts/category/${categoryId}`,
    )
    console.log(response)
    if (!response.ok) {
        throw new Error('Failed to fetch posts by category')
    }
    return response.json()
}

//인기있는 컬러 가져오기
export const fetchPopularColor = async (): Promise<PopularColor[]> => {
    const response = await fetch(`${API_PUBLIC_URL}/posts/popular_color`)
    if (!response.ok) {
        throw new Error('Failed to fetch popular colors')
    }
    return response.json()
}

// Fetch posts by category and RGB color
export const fetchPostsByCategoryAndColor = async (
    condition: CategoryAndColorSearchCondition,
    page: number,
    size: number,
): Promise<PageResponse<PostDtoResponse>> => {
    const response = await fetch(`${API_PUBLIC_URL}/posts/search_by_category`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(condition),
    })
    console.log(response)
    if (!response.ok) {
        throw new Error('Failed to fetch posts by category and color')
    }
    return response.json()
}
