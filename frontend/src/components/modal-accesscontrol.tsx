import React from 'react'

interface ModalProps {
    show: boolean
    onClose: () => void
}

const Modal: React.FC<ModalProps> = ({ show, onClose }) => {
    if (!show) return null

    return (
        <div className='fixed inset-0 z-50 flex items-center justify-center'>
            <div className='fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm'></div>
            <div className='relative bg-white p-6 rounded-lg shadow-lg max-w-sm text-center z-10'>
                <h2 className='text-xl font-semibold mb-4'>회원가입 필요</h2>
                <p className='mb-4'>
                    해당 페이지에 접근하려면 회원가입이 필요합니다.
                </p>
                <button
                    className='bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600'
                    onClick={onClose}
                >
                    닫기
                </button>
                <a href='/posts' className='block mt-4 text-blue-500 underline'>
                    포스트 페이지로 연결하기
                </a>
            </div>
        </div>
    )
}

export default Modal
