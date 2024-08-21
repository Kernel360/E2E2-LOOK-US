'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { FaCamera } from 'react-icons/fa'
import {
    myInfoAllFunction,
    updateProfileInfo,
    myInfoAllResponse,
} from '@/app/_api/myPage'
import ProfileImageModal from '@/components/profile-image-modal'
import './profileEdit.scss' // 스타일 파일 임포트
import { API_PUBLIC_URL } from '@/app/_common/constants'

export default function ProfileEditPage() {
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [previewImage, setPreviewImage] = useState<string | null>(null)
    const [profileImage, setProfileImage] = useState<File | null>(null)
    const [nickname, setNickname] = useState('')
    const [gender, setGender] = useState('UNSELECTED')
    const [birth, setBirth] = useState('')
    const [instaId, setInstaId] = useState('')
    const router = useRouter()

    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const data = await myInfoAllFunction()
                setUserInfo(data)
                setNickname(data.nickname)
                setGender(data.gender)
                setBirth(data.birth)
                setInstaId(data.instaId)
            } catch (error) {
                console.error('Failed to fetch user info:', error)
            }
        }

        fetchUserInfo()
    }, [])

    const handleProfileImageChange = (
        e: React.ChangeEvent<HTMLInputElement>,
    ) => {
        const file = e.target.files?.[0]
        if (file) {
            setProfileImage(file)
            const reader = new FileReader()
            reader.onloadend = () => {
                setPreviewImage(reader.result as string)
            }
            reader.readAsDataURL(file)
            setIsModalOpen(true) // 파일 선택 시 모달 오픈
        }
    }

    const handleSaveChanges = async () => {
        try {
            const updateRequest = {
                birth,
                instaId,
                nickName: nickname,
                gender,
            }

            // await updateProfileInfo(updateRequest, profileImage)
            router.push('/mypage') // 수정 완료 후 마이페이지로 이동
        } catch (error) {
            console.error('Failed to update profile:', error)
        }
    }

    if (!userInfo) return <div>Loading...</div>

    return (
        <div className='profile-edit-container'>
            <h1 className='profile-edit-title'>프로필 수정</h1>
            <form className='profile-edit-form'>
                <label className='profile-edit-label'>
                    이메일
                    <input
                        type='email'
                        value={userInfo.email}
                        readOnly
                        className='profile-edit-input'
                    />
                </label>
                <label className='profile-edit-label'>
                    성별
                    <select
                        value={gender}
                        onChange={e => setGender(e.target.value)}
                        className='profile-edit-select'
                    >
                        <option value='MAN'>남자</option>
                        <option value='WOMAN'>여자</option>
                        <option value='UNSELECTED'>선택 안함</option>
                    </select>
                </label>
                <label className='profile-edit-label'>
                    생년월일
                    <input
                        type='date'
                        value={birth}
                        onChange={e => setBirth(e.target.value)}
                        className='profile-edit-input'
                    />
                </label>
                <label className='profile-edit-label'>
                    닉네임
                    <input
                        type='text'
                        value={nickname}
                        onChange={e => setNickname(e.target.value)}
                        className='profile-edit-input'
                    />
                </label>
                <label className='profile-edit-label'>
                    인스타그램 ID
                    <input
                        type='text'
                        value={instaId}
                        onChange={e => setInstaId(e.target.value)}
                        className='profile-edit-input'
                    />
                </label>
                <label className='profile-edit-label'>
                    프로필 이미지
                    <div className='profile-image-upload'>
                        <input
                            type='file'
                            accept='image/*'
                            onChange={handleProfileImageChange}
                            className='profile-image-input'
                        />
                        <div className='profile-image-preview'>
                            {previewImage ? (
                                <img
                                    src={previewImage}
                                    alt='Profile Preview'
                                    className='profile-image'
                                />
                            ) : (
                                <img
                                    src={`${API_PUBLIC_URL}/image/${userInfo.imageId}`}
                                    alt='Current Profile'
                                    className='profile-image'
                                />
                            )}
                            <FaCamera className='profile-camera-icon' />
                        </div>
                    </div>
                </label>
                <button
                    type='button'
                    onClick={handleSaveChanges}
                    className='profile-edit-button'
                >
                    저장하기
                </button>
            </form>

            {/* 프로필 이미지 모달 */}
            <ProfileImageModal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                onSave={(file: File) => setProfileImage(file)}
                previewImage={previewImage}
            />
        </div>
    )
}
