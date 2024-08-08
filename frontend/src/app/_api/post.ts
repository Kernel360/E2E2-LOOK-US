import { ApiError } from "next/dist/server/api-utils";
import { API_PRIVATE_URL, API_PUBLIC_URL } from "../_common/constants";

export interface GetPostResponse {
    userId: number,
    postId: number,
    imageId: number,
    postContent: string,
    hashtagContents: string[],
    likeCount: number,
    createdAt: Date,
    updatedAt: Date,
}

export async function getPost(postId: number) {

    const requestUrl = `${API_PUBLIC_URL}/posts/${postId}`;
    const res = await fetch(requestUrl, {
        method: "GET",
    })
    const body = await res.json();

    if (false === res.ok) {
        // ...
        throw new ApiError(res.status, body);
    }

    return await res.json() as GetPostResponse;
}