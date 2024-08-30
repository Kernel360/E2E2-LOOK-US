'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import './LoginPage.scss'
import { API_OAUTH2_URL } from '@/app/_common/constants'
import Image from 'next/image'

export default function LoginPage() {
    const router = useRouter()
    const [isLoading, setIsLoading] = useState(false)

    const handleGoogleLogin = async () => {
        setIsLoading(true)
        try {
            console.log('login')
            router.push(`${API_OAUTH2_URL}/google`) // 실제 OAuth2 로그인 URL로 대체
            //
        } catch (error) {
            console.error('Google login failed:', error)
            setIsLoading(false)
        }
    }

    return (
        <div className='login-container'>
            <div className='login-background'>
                <Image
                    src='/images/loginsplashbg.png'
                    alt='Login Background'
                    layout='fill' // 배경 이미지를 화면에 꽉 채우기 위해 사용
                    objectFit='cover'
                    priority={true}
                />
            </div>

            <div className='login-content'>
                <button
                    onClick={handleGoogleLogin}
                    disabled={isLoading}
                    className='login-button'
                >
                    <Image
                        src='/images/googleloginbtn.png'
                        alt='Google Login Button'
                        width={300} // 버튼 이미지의 너비 설정
                        height={50} // 버튼 이미지의 높이 설정
                        priority={true}
                    />
                </button>
            </div>
        </div>
    )
}
