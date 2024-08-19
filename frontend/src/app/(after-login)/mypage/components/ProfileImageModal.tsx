import React, { useState } from 'react'

import './ProfileImageModal.scss' // 스타일 파일 임포트

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
        <div className='modal-overlay'>
            <div className='modal-content'>
                <h2 className='modal-title'>프로필 이미지 변경</h2>
                <input type='file' onChange={handleFileChange} />
                <div className='modal-actions'>
                    <button onClick={onClose}>취소</button>
                    <button onClick={handleSave}>저장</button>
                </div>
            </div>
        </div>
    )
}

export default ProfileImageModal
