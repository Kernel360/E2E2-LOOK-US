import React, { useState } from 'react'
import { postPreviewContent } from '@/app/_api/fetchStyle'
import Link from 'next/link'
import { API_PUBLIC_URL } from '@/app/_common/constants'
import Image from 'next/image'
import '@/app/(after-login)/posts/gallery.scss'
import { cn } from '@/lib/utils'

export default function StylePreview({
    content,
    className,
}: {
    content: postPreviewContent
    className?: string
}) {
    const [isHovered, setIsHovered] = useState(false)

    return (
        <Link
            className={cn(className, 'relative block')}
            href={`/posts/${content.postId}`}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <div className='overflow-hidden group'>
                <Image
                    className='transition-all duration-300 transform group-hover:scale-105'
                    src={`${API_PUBLIC_URL}/image/${content.imageId}`}
                    alt='style'
                    unoptimized={true}
                    priority={true}
                    width={400}
                    height={600}
                    sizes='250px'
                />
                {isHovered && (
                    <div className='absolute bottom-0 left-0 right-0 bg-black bg-opacity-40 flex justify-around items-center p-1 text-white text-xs'>
                        <span>조회수: {content.hits}</span>
                        <span>좋아요: {content.likeCount}</span>
                    </div>
                )}
            </div>
        </Link>
    )
}
