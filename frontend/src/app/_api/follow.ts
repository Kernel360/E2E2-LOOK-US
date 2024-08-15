import { ApiError } from 'next/dist/server/api-utils'
import { API_PRIVATE_URL } from '../_common/constants'

export interface FollowRequest {
    nickname: string
    followStatus: number
}

export async function follow(request?: FollowRequest) {
    const requestUrl = `${API_PRIVATE_URL}/me/follow`

    // 요청에 필요한 데이터를 전달
    const res = await fetch(requestUrl, {
        method: 'PUT',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json', // 요청의 콘텐츠 타입을 JSON으로 설정
        },
        body: JSON.stringify(request), // FollowRequest 데이터를 JSON으로 직렬화하여 전송
    })

    const body = await res.json()

    if (false === res.ok) {
        throw new ApiError(res.status, body)
    }

    return body // API 응답을 반환
}
