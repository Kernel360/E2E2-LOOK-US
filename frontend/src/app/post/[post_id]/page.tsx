'use client'

import { getPost } from '@/app/_api/post';
import { API_PRIVATE_URL } from '@/app/_common/constants';
import { Metadata, ResolvingMetadata } from 'next'
import Image from 'next/image';
import { Suspense } from 'react';
import { twMerge } from 'tailwind-merge';

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


export async function PostImage({
	className,
	imageId,
}: {
	className?: string,
	imageId: number,
}) {

	return (
		<Image
			className={twMerge(className, "")}
			src={`${API_PRIVATE_URL}/image/${imageId}`}
			alt='style'
			width={"300"}
			height={"300"}
			unoptimized={true}
		/>
	)
}

export default async function Page({ params, searchParams }: Props) {

	const post = await getPost(params.post_id);

	return (
		<>
			<h1>{`User ID : ${post.userId}`}</h1>
			{/* <Suspense fallback={<div>Loading...</div>}> */}
				<PostImage imageId={post.imageId} />
				<p>hashTag : {`${post.hashtagContents}`}</p>
				<p>postContent : {`${post.postContent}`}</p>
			{/* </Suspense> */}
		</>
	);
}