/**
 * v0 by Vercel.
 * @see https://v0.dev/t/MeSpDnKyjpf
 * Documentation: https://v0.dev/docs#integrating-generated-code-into-your-nextjs-app
 */
'use client'
import { useEffect, useState } from 'react'
import ProfileImageModal from './ProfileImageModal' // Modal 컴포넌트 임포트
import {
    updateProfileImg,
    myInfoAllFunction,
    myInfoAllResponse,
} from '@/app/_api/myPage'
import { API_PUBLIC_URL } from '@/app/_common/constants'

export default function MyPageInfoComponent() {
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [isModalOpen, setIsModalOpen] = useState(false)

    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const data = await myInfoAllFunction()
                setUserInfo(data)
            } catch (error) {
                console.error('Failed to fetch user info:', error)
            }
        }

        fetchUserInfo()
    }, [])

    const handleProfileImageSave = async (file: File) => {
        try {
            await updateProfileImg(file)
            const updatedUserInfo = await myInfoAllFunction() // 업데이트된 정보 다시 가져오기
            setUserInfo(updatedUserInfo)
        } catch (error) {
            console.error('Failed to update profile image:', error)
        }
    }

    if (!userInfo) return <div>Loading...</div>

    return (
        <div>
            <div className='px-4 space-y-6 sm:px-6'>
                <header className='space-y-2'>
                    <div className='flex items-center space-x-3'>
                        <img
                            src={`${API_PUBLIC_URL}/image/${userInfo.imageId}`}
                            alt='Avatar'
                            width='96'
                            height='96'
                            className='rounded-full'
                            style={{ aspectRatio: '96/96', objectFit: 'cover' }}
                        />
                        <div className='space-y-1'>
                            <h1 className='text-2xl font-bold'>
                                {userInfo.nickname}
                            </h1>
                            <button
                                className='px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md'
                                onClick={() => setIsModalOpen(true)}
                            >
                                프로필 이미지 변경
                            </button>
                        </div>
                    </div>
                </header>
            </div>

            <ProfileImageModal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                onSave={handleProfileImageSave}
            />
        </div>
    )
}
