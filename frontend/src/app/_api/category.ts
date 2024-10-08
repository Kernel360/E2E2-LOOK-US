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
    categoryId: number // 변경된 부분
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
    categoryId: number, // 변경된 부분
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

// 색상으로 게시물 가져오기
export const fetchPostsByColor = async (
    rgbColor: number[],
    page: number,
    size: number,
): Promise<PageResponse<PostDtoResponse>> => {
    // RGB 값을 개별 쿼리 파라미터로 변환
    const queryParams = `rgbColor=${rgbColor[0]}&rgbColor=${rgbColor[1]}&rgbColor=${rgbColor[2]}`
    console.log(queryParams)

    const response = await fetch(
        `${API_PUBLIC_URL}/posts?${queryParams}&page=${page}&size=${size}`,
    )
    if (!response.ok) {
        throw new Error('Failed to fetch posts by color')
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
    console.log('Condition before API call:', condition) // 디버깅 로그 추가

    if (condition.categoryId === 0) {
        // 0으로 전체 카테고리 조회하는 예시
        console.log('wow')
        return fetchPostsByColor(condition.rgbColor, page, size)
    }

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
