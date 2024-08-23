'use client'

import * as React from 'react'

import { cn } from '@/lib/utils'
import { Icons } from './Icons'
import Link from 'next/link'
import { buttonVariants } from './ui/button'
import { API_OAUTH2_URL } from '@/app/_common/constants'

interface UserAuthFormProps extends React.HTMLAttributes<HTMLDivElement> {}

export function UserAuthForm({ className, ...props }: UserAuthFormProps) {
    return (
        <div className={cn('grid gap-6', className)} {...props}>
            <Link
                href={`${API_OAUTH2_URL}/google`}
                className={cn(buttonVariants(), 'rounded-[6px]')}
            >
                <Icons.google className='mr-2 h-4 w-4' />
                Google
            </Link>
        </div>
    )
}
