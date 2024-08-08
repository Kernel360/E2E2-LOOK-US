"use client"


import { Suspense } from 'react'
import { getAllPostPreviews } from '../_api/postPreview'
import PostPreview from '@/components/post-preview';

async function Posts() {

	// TODO : 무한 스크롤을 Next.js에서 어떻게 하는지 찾아보자...!
	const postPreviewAllResponse = await getAllPostPreviews();

	const postPreviewList = postPreviewAllResponse.postResponseDtoList;

	return (
		<section>
			<Suspense fallback={<p>Loading feed...</p>}>
				<div
					className='
						mt-7 px-2 md:px-5 
						columns-2 md:columns-3 lg:columns-4 
						mb-4 xl:columns-5 space-y-6 mx-auto'
				>
					{postPreviewList.map((item, index) => (
						<PostPreview postPreviewData={item}/>
					))}
				</div>
			</Suspense>
		</section>
	)
}

export default Posts;