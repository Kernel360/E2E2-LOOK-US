import { ApiError } from "next/dist/server/api-utils";
import { API_PRIVATE_URL, API_PUBLIC_URL } from "../_common/constants";
import { PostFormValues } from "@/components/post-create";

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

export interface CreatePostRequest {
    image: any;
    userRequest:{
        post_content : string,
        hashtag_content : string
    } 
}

export async function createPost(form: PostFormValues) {
    const requestUrl = `${API_PRIVATE_URL}/posts`;

    // https://stackoverflow.com/questions/16245767/creating-a-blob-from-a-base64-string-in-javascript
    const reqeustBody: CreatePostRequest = {
        image: form.image,
        userRequest: {
            post_content: form.content,
            hashtag_content: `#${form.hashtags?.join("#")}`
        }
    }

    const res = await fetch(requestUrl, {
        method: 'POST',
        body: JSON.stringify(reqeustBody),
        // image file (form-data)
        // headers: { "Content-Type": "multipart/form-data" }, // 이 부분 세팅하지 말라는 말이 있음.
        // (https://muffinman.io/blog/uploading-files-using-fetch-multipart-form-data/)
    });


    if (false === res.ok) {
        // ...
        // const body = await res.json();
        throw new ApiError(res.status, "error");
    }
}
