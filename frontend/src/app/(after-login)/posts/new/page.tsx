'use client'

import { PostContentForm } from '@/components/post-create'
import Modal from '@/components/modal-accesscontrol'
import { useAuth } from '@/app/_api/useAuth'

export default function PostCreateForm() {
    const { showModal, handleCloseModal } = useAuth() // useAuth 훅을 사용하여 모달 제어

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
