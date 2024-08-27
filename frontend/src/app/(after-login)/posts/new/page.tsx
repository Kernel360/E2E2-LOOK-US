'use client'

import { PostContentForm } from '@/components/post-create'
import { useRouter } from 'next/navigation'
import { parseCookies } from 'nookies'
import Modal from '@/components/modal-accesscontrol'
import { useEffect, useState } from 'react'

export default function PostCreateForm() {
    const [showModal, setShowModal] = useState(false)
    const router = useRouter()

    useEffect(() => {
        const cookies = parseCookies()
        // const token = cookies.token
        // console.log('Parsed Cookies:', token) // 여기에서 쿠키가 올바르게 파싱되는지 확인

        if (!cookies) {
            setShowModal(true)
            return
        }
    }, [router])

    const handleCloseModal = () => {
        setShowModal(false)
        router.push('/posts')
    }

    return (
        <div className='space-y-6 p-10 pb-16 md:block'>
            <div className='space-y-0.5'>
                <h2 className='text-2xl font-bold tracking-tight'>
                    글 작성하기
                </h2>
            </div>

            <div className='flex flex-col space-y-8 lg:flex-row'>
                <div className='flex-1'>
                    <PostContentForm />
                </div>
            </div>

            <Modal show={showModal} onClose={handleCloseModal} />
        </div>
    )
}
