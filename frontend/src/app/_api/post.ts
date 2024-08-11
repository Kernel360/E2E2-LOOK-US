import { ApiError } from 'next/dist/server/api-utils'
import { API_PRIVATE_URL, API_PUBLIC_URL } from '../_common/constants'
import { PostFormValues } from '@/components/post-create'

import { getCookie, setCookie } from 'cookies-next'

export interface GetPostResponse {
    nickname: string
    postId: number
    imageId: number
    postContent: string
    hashtagContents: string[]
    likeCount: number
    createdAt: Date
    updatedAt: Date
}

export async function getPost(postId: number) {
    const requestUrl = `${API_PUBLIC_URL}/posts/${postId}`
    const res = await fetch(requestUrl, {
        method: 'GET',
    })
    // const body = await res.json();

    if (false === res.ok) {
        // ...
        // throw new ApiError(res.status, body)
    }

    return (await res.json()) as GetPostResponse
}

export interface CreatePostRequest {
    image: any
    userRequest: {
        post_content: string
        hashtag_content: string
    }
}

// Create Blob file from URL
const dataURLtoBlob = (dataUrl: string) => {
    const arr = dataUrl.split(',')

    const mimeMatch = arr[0].match(/:(.*?);/)
    if (!mimeMatch || mimeMatch.length < 2) {
        throw new Error('Invalid data URL')
    }

    const mime = mimeMatch[1]
    const bstr = atob(arr[1])
    const n = bstr.length
    const u8arr = new Uint8Array(n)

    for (let i = 0; i < n; i++) {
        u8arr[i] = bstr.charCodeAt(i)
    }

    return new Blob([u8arr], { type: mime })
}

/**
 * - How to send image blob to server.
 * https://advanced-cropper.github.io/react-advanced-cropper/docs/guides/recipes/
 */
export async function createPost(form: PostFormValues) {
    const requestUrl = `${API_PRIVATE_URL}/posts`
    const formData = new FormData()

    // 1. append image blob (file format is already set in Blob object)
    formData.append('image', form.editedImageBlob)

    // 2. append user input data (content, hashtag, etc...)
    const data = new Blob(
        [
            JSON.stringify({
                post_content: form.content,
                hashtag_content: `#${form.hashtags?.map(tag => tag.value).join('#')}`,
            }),
        ],
        {
            type: 'application/json',
        },
    )
    formData.append('userRequest', data)

    // 3. send image to server
    const res = await fetch(requestUrl, {
        method: 'POST',
        credentials: 'include',
        headers: {
            Authorization: 'Bearer ' + getCookie('token'),
        },
        body: formData,
    })

    if (!res.ok) {
        // ...
        const body = await res.json()
        throw new ApiError(res.status, body)
    }
}
