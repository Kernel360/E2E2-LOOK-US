'use client'

import { ChangeEvent, useState } from 'react'
import { useRouter } from 'next/navigation'
import { FaCamera } from 'react-icons/fa'
import { updateProfileInfo } from '@/app/_api/myPage' // updateProfileInfo 함수 import
import { getMimeType } from 'advanced-cropper/extensions/mimes'
import './signup.scss'
import { on } from 'process'
import { parseCookies } from 'nookies'

export interface ProfileImage {
    blob: Blob
    mimeType: string
}

export default function SignupPage() {
    const [step, setStep] = useState(1)
    const [birth, setBirth] = useState('')
    const [instaId, setInstaId] = useState('')
    const [nickName, setNickName] = useState('')
    const [gender, setGender] = useState('UNSELECTED')
    const [profileImage, setProfileImage] = useState<ProfileImage | null>(null)
    const [previewImage, setPreviewImage] = useState<string | null>(null)
    const router = useRouter()
    const cookies = parseCookies()

    const handleNextStep = () => {
        if (step < 4) {
            setStep(step + 1)
        } else {
            handleSignup()
        }
    }

    const handleSignup = async () => {
        try {
            const updateRequest = {
                birth,
                instaId,
                nickName,
                gender,
            }

            // updateProfileInfo 함수 호출
            await updateProfileInfo(updateRequest, profileImage)

            // 회원가입 완료 후 메인 페이지로 이동
            router.push('/posts')
        } catch (error) {
            console.error('Failed to sign up:', error)
        }
    }

    const handleProfileImageChange = (
        e: React.ChangeEvent<HTMLInputElement>,
    ) => {
        const file = e.target.files?.[0]
        // Remember the fallback type:
        const typeFallback = e.target.files?.[0].type

        if (file && typeFallback) {
            setProfileImage({ blob: file, mimeType: typeFallback })
            const reader = new FileReader()
            reader.onloadend = () => {
                setPreviewImage(reader.result as string)
            }
            reader.readAsDataURL(file)
        }
    }

    return (
        <div className='signup-container'>
            <div className='signup-box'>
                {step === 1 && (
                    <div className='signup-step'>
                        <label className='signup-label'>
                            생년월일
                            <input
                                type='date'
                                value={birth}
                                onChange={e => setBirth(e.target.value)}
                                className='signup-input'
                            />
                        </label>
                        <button
                            onClick={handleNextStep}
                            className='signup-button'
                        >
                            다음
                        </button>
                    </div>
                )}
                {step === 2 && (
                    <div className='signup-step'>
                        <label className='signup-label'>
                            인스타그램 ID
                            <input
                                type='text'
                                value={instaId}
                                onChange={e => setInstaId(e.target.value)}
                                className='signup-input'
                                placeholder='인스타그램 ID를 입력하세요.'
                            />
                        </label>
                        <button
                            onClick={handleNextStep}
                            className='signup-button'
                        >
                            다음
                        </button>
                    </div>
                )}
                {step === 3 && (
                    <div className='signup-step'>
                        <label className='signup-label'>
                            닉네임
                            <input
                                type='text'
                                value={nickName}
                                onChange={e => setNickName(e.target.value)}
                                className='signup-input'
                                placeholder='닉네임을 입력하세요.'
                            />
                        </label>
                        <button
                            onClick={handleNextStep}
                            className='signup-button'
                        >
                            다음
                        </button>
                    </div>
                )}
                {step === 4 && (
                    <div className='signup-step'>
                        <label className='signup-label'>
                            성별
                            <div className='signup-gender'>
                                <button
                                    onClick={() => setGender('MAN')}
                                    className={`signup-gender-button ${
                                        gender === 'MAN' ? 'active' : ''
                                    }`}
                                >
                                    남자
                                </button>
                                <button
                                    onClick={() => setGender('WOMAN')}
                                    className={`signup-gender-button ${
                                        gender === 'WOMAN' ? 'active' : ''
                                    }`}
                                >
                                    여자
                                </button>
                                <button
                                    onClick={() => setGender('NONE')}
                                    className={`signup-gender-button ${
                                        gender === 'NONE' ? 'active' : ''
                                    }`}
                                >
                                    선택 <br />
                                    안함
                                </button>
                            </div>
                        </label>
                        <label className='signup-label'>
                            프로필 이미지
                            <div className='signup-image-upload'>
                                <input
                                    type='file'
                                    accept='image/*'
                                    onChange={handleProfileImageChange}
                                    className='signup-image-input'
                                />
                                <div className='signup-image-preview'>
                                    {previewImage ? (
                                        <img
                                            src={previewImage}
                                            alt='Profile Preview'
                                        />
                                    ) : (
                                        <FaCamera className='signup-camera-icon' />
                                    )}
                                </div>
                            </div>
                        </label>
                        <button
                            onClick={handleNextStep}
                            className='signup-button'
                        >
                            완료
                        </button>
                    </div>
                )}
            </div>
        </div>
    )
}
