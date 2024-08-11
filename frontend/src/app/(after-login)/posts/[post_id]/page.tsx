'use client'

import { getPost } from '@/app/_api/post'
import { Suspense } from 'react'

// ----------------------------------------------------------------
/**
 * https://nextjs.org/docs/app/api-reference/functions/generate-metadata
 *
 * Dynamic Metadata generation for Style Feed
 */

type Props = {
    params: { post_id: number }
    searchParams: { [key: string]: string | string[] | undefined }
}

export default async function Page({ params, searchParams }: Props) {
    const post = await getPost(params.post_id)

    return (
        <>
            <h1>{`User ID : ${post.userId}`}</h1>
            <Suspense fallback={<div>Loading...</div>}>
                <p>hashTag : {`${post.hashtagContents}`}</p>
                <p>postContent : {`${post.postContent}`}</p>
            </Suspense>
        </>
    )
}
