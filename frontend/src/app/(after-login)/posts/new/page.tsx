import * as React from 'react'

import { Separator } from '@/components/ui/separator'
import { PostContentForm } from '@/components/post-create'
import { cookies } from 'next/headers'

export default function PostCreateForm() {
    return (
        <div className='space-y-6 p-10 pb-16 md:block'>
            <div className='space-y-0.5'>
                <h2 className='text-2xl font-bold tracking-tight'>
                    글 작성하기
                </h2>
                <p className='text-muted-foreground'>
                    Manage your account settings and set e-mail preferences.
                </p>
            </div>

            <div className='flex flex-col space-y-8 lg:flex-row'>
                <div className='flex-1'>
                    <PostContentForm accessToken={cookies().get('token')} />
                </div>
            </div>
        </div>
    )
}
