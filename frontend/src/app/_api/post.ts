// src/app/_api/posts.ts

import { ApiError } from 'next/dist/server/api-utils'
import { API_PRIVATE_URL, API_PUBLIC_URL } from '../_common/constants'

// 게시글 정보 가져오기 🎀
export interface GetPostResponse {
    nickname: string
    profileImageLocationId: number
    postId: number
    imageLocationId: number
    postContent: string
    hashtagContents: string[]
    categories: string[]
    likeCount: number
    likeStatus: boolean
    hits: number
    createdAt: Date
    updatedAt: Date
}

export async function getPost(postId: number) {
    const requestUrl = `${API_PUBLIC_URL}/posts/${postId}`
    const res = await fetch(requestUrl, {
        method: 'GET',
        credentials: 'include',
    })

    const body = await res.json()

    if (!res.ok) {
        throw new ApiError(res.status, body)
    }

    return body as GetPostResponse
}

// 게시글 작성 요청 🎀
export async function createPost(formData: FormData) {
    const requestUrl = `${API_PRIVATE_URL}/posts`

    // 서버로 전송할 요청 생성
    const res = await fetch(requestUrl, {
        method: 'POST',
        credentials: 'include',
        body: formData, // FormData 객체 전송
    })

    if (!res.ok) {
        const body = await res.json()
        throw new ApiError(res.status, body)
    }
}

// 좋아요 요청 🎀
export async function likePost(postId: number) {
    const requestUrl = `${API_PRIVATE_URL}/posts/likes`

    const res = await fetch(requestUrl, {
        method: 'PATCH',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ postId }),
    })

    if (!res.ok) {
        const body = await res.json()
        throw new ApiError(res.status, body)
    }

    return await res.json() // 좋아요 상태 반환
}

// 카테고리 목록 가져오기 🎀
export async function fetchCategories() {
    const response = await fetch(`${API_PUBLIC_URL}/posts/categoryAll`)
    if (!response.ok) {
        throw new Error('Failed to fetch categories')
    }
    return response.json()
}
