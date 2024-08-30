'use client'

import UploadOOTD from '@/components/post-create'
import Modal from '@/components/modal-accesscontrol'
import { useAuth } from '@/app/_api/useAuth'

export default function PostCreateForm() {
    const { showModal, handleCloseModal } = useAuth()

    return (
        <div className='post-create-form'>
            <UploadOOTD />
            <Modal show={showModal} onClose={handleCloseModal} />
        </div>
    )
}
