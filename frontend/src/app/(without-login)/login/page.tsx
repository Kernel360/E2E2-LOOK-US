import { Metadata } from 'next'
import Link from 'next/link'
import Image from 'next/image'

import { cn } from '@/lib/utils'
import { buttonVariants } from '@/components/ui/button'
import { UserAuthForm } from '@/components/user-auth-form-simple'
import { API_PRIVATE_URL, API_PUBLIC_URL } from '../../_common/constants'

export const metadata: Metadata = {
    title: 'Authentication',
    description: 'Authentication forms built using the components.',
}

export default function AuthenticationPage() {
    return (
        <>
            <div className='container relative hidden h-[800px] flex-col items-center justify-center md:grid lg:max-w-none lg:grid-cols-2 lg:px-0'>
                <div className='relative hidden h-full flex-col bg-muted p-10 text-white lg:flex dark:border-r'>
                    {/* TODO: use today's best style image */}
                    <div className='absolute inset-0 bg-zinc-900'>
                        <img
                            className=' bg-cover w-full'
                            src={`${API_PUBLIC_URL}/image/${1}`}
                        />
                    </div>
                    <div className='relative z-20 flex items-center text-lg font-medium'>
                        <svg
                            xmlns='http://www.w3.org/2000/svg'
                            viewBox='0 0 24 24'
                            fill='none'
                            stroke='currentColor'
                            strokeWidth='2'
                            strokeLinecap='round'
                            strokeLinejoin='round'
                            className='mr-2 h-6 w-6'
                        >
                            <path d='M15 6v12a3 3 0 1 0 3-3H6a3 3 0 1 0 3 3V6a3 3 0 1 0-3 3h12a3 3 0 1 0-3-3' />
                        </svg>
                        대표 사용자 닉네임
                    </div>
                    <div className='relative z-20 mt-auto'>
                        <blockquote className='space-y-2'>
                            <p className='text-lg'>
                                &ldquo;This library has saved me countless hours
                                of work and helped me deliver stunning designs
                                to my clients faster than ever before.&rdquo;
                            </p>
                            <footer className='text-sm'>Sofia Davis</footer>
                        </blockquote>
                    </div>
                </div>
                <div className='lg:p-8'>
                    <div className='mx-auto flex w-full flex-col space-y-6 sm:w-[350px]'>
                        <div className='flex flex-col space-y-2 '>
                            <h1 className='text-3xl font-semibold tracking-tight'>
                                Sign In
                            </h1>
                            <p className='text-sm text-muted-foreground'>
                                Start STYLE:US with social account
                            </p>
                        </div>
                        <UserAuthForm />
                    </div>
                </div>
            </div>
        </>
    )
}
