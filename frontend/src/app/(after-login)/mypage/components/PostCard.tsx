// components/PostCard.tsx
import Image from 'next/image'
import { API_PRIVATE_URL, API_PUBLIC_URL } from '@/app/_common/constants'
import Link from 'next/link'

interface PostCardProps {
    imageId: number
    postContent: string
    likeCount: number
    postId: number
}

export function PostCard({
    imageId,
    postContent,
    likeCount,
    postId,
}: PostCardProps) {
    const truncatedContent =
        postContent.length > 10 ? postContent.slice(0, 10) + '...' : postContent

    const onClickPostCard = () => {}
    return (
        <div className='w-[300px] space-y-3'>
            <div className='overflow-hidden rounded-md'>
                <Link href={`/posts/${postId}`}>
                    <Image
                        src={`${API_PUBLIC_URL}/image/${imageId}`}
                        alt={truncatedContent}
                        width={300}
                        height={400}
                        className='h-auto w-auto object-cover transition-all hover:scale-105 aspect-square'
                        onClick={onClickPostCard}
                    />
                </Link>
            </div>
            <div className='space-y-1 text-sm'>
                <h3 className='font-medium leading-none'>{truncatedContent}</h3>
                <p className='text-xs text-muted-foreground'>
                    좋아요 {likeCount}개
                </p>
            </div>
        </div>
    )
}
