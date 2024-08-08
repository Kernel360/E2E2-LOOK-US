import React, { Suspense } from 'react'
import { useRouter } from 'next/navigation'
import { PostImage } from '../app/post/[post_id]/page';

export interface PostPreviewProps {
    userId: number,
    postId: number,
    imageId: number,
    postContent: string,
    hashtagContents: string[],
    likeCount: number,
    createdAt: Date,
    updatedAt: Date,
}

async function PostPreview({
    postPreviewData
}: {
    postPreviewData: PostPreviewProps
}) {

    const router = useRouter();

    return (
        <div className=''>
            <div className="
        relative before:absolute
        before:h-full before:w-full
        before:rounded-3xl
        before:z-10
        hover:before:bg-gray-600 
        before:opacity-50
        cursor-pointer
    "
                onClick={() => router.push(`/post/${postPreviewData.postId}`)}>
                <Suspense fallback={<div>Loading...</div>}>
                    <PostImage
                        className='rounded-3xl cursor-pointer relative z-0'
                        imageId={postPreviewData.imageId}
                    />
                </Suspense>
            </div>
            <p>hashTag : {`${postPreviewData.hashtagContents}`}</p>
            <p>postContent : {`${postPreviewData.postContent}`}</p>
        </div>
    )
}

export default PostPreview;