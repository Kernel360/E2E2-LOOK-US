'use client'
import { useEffect, useState } from 'react'
import { ScrollArea, ScrollBar } from '@/components/ui/scroll-area'
import { Separator } from '@/components/ui/separator'
import MyPageInfoComponent from './components/mypageinfo'
import { getMyPosts, myPostAllResponse } from '@/app/_api/myPage'
import { PostCard } from './components/PostCard'

export default function MyPage() {
    const [myPosts, setMyPosts] = useState<myPostAllResponse[]>([])

    useEffect(() => {
        async function fetchPosts() {
            try {
                const posts = await getMyPosts()
                setMyPosts(posts)
            } catch (error) {
                console.error('Failed to fetch posts:', error)
            }
        }
        fetchPosts()
    }, [])

    return (
        <div className='container'>
            <MyPageInfoComponent />
            <div className='hidden md:block'>
                <div className='border-t'>
                    <div className='bg-background'>
                        <div className='grid lg:grid-cols-5'>
                            <div className='col-span-3 lg:col-span-4 lg:border-l'>
                                <div className='h-full px-4 py-6 lg:px-8'>
                                    <div className='space-y-6'>
                                        <div className='flex items-center justify-between'>
                                            <div className='space-y-1'>
                                                <h2 className='text-2xl font-semibold tracking-tight'>
                                                    내 업로드
                                                </h2>
                                                <p className='text-sm text-muted-foreground'>
                                                    내가 업로드한 글 확인하기
                                                </p>
                                            </div>
                                        </div>
                                        <Separator className='my-4' />
                                        <div className='relative'>
                                            <ScrollArea>
                                                <div className='flex space-x-4 pb-4'>
                                                    {myPosts.map(post => (
                                                        <PostCard
                                                            key={post.imageId}
                                                            imageId={
                                                                post.imageId
                                                            }
                                                            postContent={
                                                                post.postContent
                                                            }
                                                            likeCount={
                                                                post.likeCount
                                                            }
                                                            postId={post.postId}
                                                        />
                                                    ))}
                                                </div>
                                                <ScrollBar orientation='horizontal' />
                                            </ScrollArea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
