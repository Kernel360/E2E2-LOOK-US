'use client'

import { getPost } from '@/app/_api/post'
import { Suspense, useEffect, useState } from 'react'
import Image from 'next/image'
import styles from './Post.module.scss'
import { API_PUBLIC_URL } from '@/app/_common/constants'
import { useRouter } from 'next/navigation'

type Props = {
    params: { post_id: number }
    searchParams: { [key: string]: string | string[] | undefined }
}

export default function Page({ params, searchParams }: Props) {
    const [post, setPost] = useState<any>(null)
    const router = useRouter()

    useEffect(() => {
        const fetchPost = async () => {
            const post = await getPost(params.post_id)
            setPost(post)
        }
        fetchPost()
    }, [params.post_id])

    const handleHashtagClick = (hashtag: string) => {
        // router.push(`/search?hashtags=${encodeURIComponent(hashtag)}`)
    }

    if (!post) return <div>Loading...</div>

    return (
        <div className={styles.container}>
            <div className={styles.imageContainer}>
                <Image
                    src={`${API_PUBLIC_URL}/image/${post.imageId}`}
                    alt='style'
                    unoptimized={true}
                    priority={true}
                    width={600}
                    height={800}
                    sizes='100%'
                />
            </div>
            <div className={styles.header}>
                <div className={styles.profileInfo}>
                    <div className={styles.profilePic}>
                        <img
                            src='https://private-user-images.githubusercontent.com/60287070/356863617-dd8f7943-e1bc-4003-bf45-78b5346b77e5.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjMzODc3MjIsIm5iZiI6MTcyMzM4NzQyMiwicGF0aCI6Ii82MDI4NzA3MC8zNTY4NjM2MTctZGQ4Zjc5NDMtZTFiYy00MDAzLWJmNDUtNzhiNTM0NmI3N2U1LnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDA4MTElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwODExVDE0NDM0MlomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTg4N2ZhZTQ3NjViNzAzMDM4YWYzZDU4ZTk0MTFhMTY0NzhiNDE4YjA3YTQ3MmU4N2M1ODJkNzk3YjRlYWRlMmEmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.2dduoN_3V1q1smBhvRAeTxMbrwpKVaLXxABmoFg_wXM'
                            alt='profile'
                        />
                    </div>
                    <span className={styles.username}>{post.nickname}</span>
                </div>
                <button className={styles.followButton}>팔로우</button>
            </div>
            <div className={styles.content}>{post.postContent}</div>
            <Suspense fallback={<div>Loading...</div>}>
                <div className={styles.hashtags}>
                    {post.hashtagContents.map((hashtag: string) => (
                        <span
                            key={hashtag}
                            className={styles.hashtag}
                            onClick={() => handleHashtagClick(hashtag)}
                        >
                            #{hashtag}
                        </span>
                    ))}
                </div>
            </Suspense>
        </div>
    )
}
