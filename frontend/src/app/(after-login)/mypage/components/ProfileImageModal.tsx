import React, { useState } from 'react'

interface ModalProps {
    isOpen: boolean
    onClose: () => void
    onSave: (file: File) => void
}

const ProfileImageModal: React.FC<ModalProps> = ({
    isOpen,
    onClose,
    onSave,
}) => {
    const [selectedFile, setSelectedFile] = useState<File | null>(null)

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files && event.target.files[0]) {
            setSelectedFile(event.target.files[0])
        }
    }

    const handleSave = () => {
        if (selectedFile) {
            onSave(selectedFile)
        }
        onClose()
    }

    if (!isOpen) return null

    return (
        <div className='fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50'>
            <div className='bg-white rounded-lg shadow-lg w-full max-w-md p-6'>
                <h2 className='text-lg font-bold mb-4'>프로필 이미지 변경</h2>
                <input type='file' onChange={handleFileChange} />
                <div className='mt-4 flex justify-end'>
                    <button
                        className='mr-2 px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md'
                        onClick={onClose}
                    >
                        취소
                    </button>
                    <button
                        className='px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md'
                        onClick={handleSave}
                    >
                        저장
                    </button>
                </div>
            </div>
        </div>
    )
}

export default ProfileImageModal
