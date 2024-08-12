import { ApiError } from 'next/dist/server/api-utils'
import { API_PUBLIC_URL } from '../_common/constants'
import { GetPostResponse } from './post'

export interface postPreviewsAllRequest {
    page?: number // 몇번째 페이지의
    size?: number // 사이즈는 몇이다.
    sort?: 'createdAt'
    direction?: 'ASC' | 'DESC'
    hashtags?: string // #123#456
    postContent?: string
}

export interface postPreviewContent {
    nickname: string
    postId: number
    imageId: number
}

// TODO: change this to preview dto later...
export interface postPreviewsAllResponse {
    totalPages: number
    totalElements: number
    first: boolean
    last: boolean
    size: number
    content: [
        {
            nickname: string
            postId: number
            imageId: number
            hashtags: []
        },
    ]
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
export async function getAllPostPreviews(request?: postPreviewsAllRequest) {
    const { page, size, sort, direction, hashtags, postContent } = request || {}

    const params = new URLSearchParams()
    if (page) params.append('page', String(page))
    if (size) params.append('size', String(size))
    if (sort) params.append('sort', sort)
    if (direction) params.append('direction', direction)
    if (hashtags) params.append('hashtags', hashtags)
    if (postContent) params.append('postContent', postContent)

    const requestUrl = `${API_PUBLIC_URL}/posts?${params.toString()}`

    const res = await fetch(requestUrl, { method: 'GET' })

    if (!res.ok) {
        const body = await res.json()
        throw new ApiError(res.status, body)
    }

    const body = await res.json()
    return body as postPreviewsAllResponse
}
