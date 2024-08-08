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

// TODO: change this to preview dto later...
export interface postPreviewsAllResponse {
    page: number, // 몇번째 페이지의 
    size: number, // 사이즈는 몇이다.
    totalElements: number, // 전체 개수
    totalPages: number,
    searchString: string,
    postResponseDtoList: GetPostResponse[], // TODO : change later
}

export async function getAllPostPreviews(
    request?: postPreviewsAllRequest
) {

    // const requestUrl = `${API_URL}/posts`;
    const requestUrl = `${API_PUBLIC_URL}/posts`;

    // console.log(requestUrl);

    const res = await fetch(requestUrl, {
        cache: "no-store",
        method: "GET",
    })

    if (false === res.ok) {
        // ...
        const body = await res.json();
        throw new ApiError(res.status, body);
    }

    const body = await res.json();
    // console.log(body);

    return body as postPreviewsAllResponse;

}