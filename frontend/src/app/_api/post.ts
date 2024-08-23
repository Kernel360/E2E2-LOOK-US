import { ApiError } from 'next/dist/server/api-utils'
import { API_PRIVATE_URL, API_PUBLIC_URL } from '../_common/constants'
import { PostFormValues } from '@/components/post-create'

export interface GetPostResponse {
    nickname: string
    postId: number
    imageId: number
    postContent: string
    hashtagContents: string[]
    likeCount: number
    likeStatus: boolean // likeStatus 필드 추가
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

    if (false === res.ok) {
        throw new ApiError(res.status, body)
    }

    return body as GetPostResponse
}

export interface CreatePostRequest {
    image: any
    userRequest: {
        post_content: string
        hashtag_content: string
    }
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
        body: formData,
    })
    console.log('성공')

    if (!res.ok) {
        // ...
        const body = await res.json()
        throw new ApiError(res.status, body)
    }
}

export async function likePost(postId: number) {
    const requestUrl = `${API_PRIVATE_URL}/posts/likes`;

    const res = await fetch(requestUrl, {
        method: 'PATCH',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ postId }),  // postId를 JSON 형식으로 전달
    });

    if (!res.ok) {
        const body = await res.json();
        throw new ApiError(res.status, body);
    }

    return await res.json(); // Returns true if liked, false if unliked
}
