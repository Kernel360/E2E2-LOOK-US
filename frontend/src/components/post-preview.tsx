import React from 'react'
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
    return (
        <Link className={cn(className, '')} href={`/posts/${content.postId}`}>
            <div className='overflow-hidden group'>
                <Image
                    className='group-hover:opacity-75'
                    src={`${API_PUBLIC_URL}/image/${content.imageLocationId}`}
                    alt='style'
                    unoptimized={true} // NOTE: used for local host... remove later
                    priority={true}
                    width={400}
                    height={600}
                    sizes='250px'
                />
            </div>
        </Link>
    )
}
