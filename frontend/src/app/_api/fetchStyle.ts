import { ApiError } from "next/dist/server/api-utils";
import { API_PUBLIC_URL } from "../_common/constants";
import { GetPostResponse } from "./post";

export interface postPreviewsAllRequest {
    page?: number, // 몇번째 페이지의 
    size?: number, // 사이즈는 몇이다.
    sortField?: 'createdAt',
    sortDirection?: 'ASC' | 'DESC',
    searchHashtagList?: string, // #123#456
    searchString?: string,
}

export interface postPreviewContent {
        nickname: string,
        postId: number,
        imageId: number,
};

// TODO: change this to preview dto later...
export interface postPreviewsAllResponse {
    totalPages: number,
    totalElements: number, // 전체 개수
    pagable: {
        pageNumber: number,
        pageSize: number,
        sort: { 
            sorted: boolean,
            empty: boolean,
            unsorted: boolean,
         },
         offset: number,
         paged: boolean,
         unpaged: boolean,
    },
    first: boolean,
    last: boolean,
    size: number,
    content: postPreviewContent[],
    numberOfElements: number,
    emtpy: boolean,
}

export async function getAllPostPreviews(
    request?: postPreviewsAllRequest
) {

    const requestUrl = `${API_PUBLIC_URL}/posts`;

    const res = await fetch(requestUrl, {method: "GET"})


    if (false === res.ok) {
        // ...
        const body = await res.json();
        throw new ApiError(res.status, body);
    }

    const body = await res.json();

    return body as postPreviewsAllResponse;

}