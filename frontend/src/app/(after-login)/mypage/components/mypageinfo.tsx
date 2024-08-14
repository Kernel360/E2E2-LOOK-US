/**
 * v0 by Vercel.
 * @see https://v0.dev/t/MeSpDnKyjpf
 * Documentation: https://v0.dev/docs#integrating-generated-code-into-your-nextjs-app
 */
'use client'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { RadioGroup } from '@/components/ui/radio-group'

export default function MyPageInfoComponent() {
    return (
        <div>
            <div className='px-4 space-y-6 sm:px-6'>
                <header className='space-y-2'>
                    <div className='flex items-center space-x-3'>
                        <img
                            src='/placeholder.svg'
                            alt='Avatar'
                            width='96'
                            height='96'
                            className='rounded-full'
                            style={{ aspectRatio: '96/96', objectFit: 'cover' }}
                        />
                        <div className='space-y-1'>
                            <h1 className='text-2xl font-bold'>사용자이름</h1>
                            <Button size='sm'>프로필 이미지 변경</Button>
                        </div>
                    </div>
                </header>
            </div>
        </div>
    )
}
