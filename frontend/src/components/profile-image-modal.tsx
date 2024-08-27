import React, { useState } from 'react'

interface ModalProps {
    isOpen: boolean
    onClose: () => void
    onSave: (file: File) => void
    previewImage: string | null
}

const ProfileImageModal: React.FC<ModalProps> = ({
    isOpen,
    onClose,
    onSave,
    previewImage,
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
                <div className='modal-body'>
                    <input type='file' onChange={handleFileChange} />
                    {previewImage && (
                        <img
                            src={previewImage}
                            alt='Profile Preview'
                            className='modal-image-preview'
                        />
                    )}
                </div>
                <div className='modal-footer'>
                    <button onClick={onClose} className='modal-cancel-button'>
                        취소
                    </button>
                    <button onClick={handleSave} className='modal-save-button'>
                        저장
                    </button>
                </div>
            </div>
        </div>
    )
}

export default ProfileImageModal
