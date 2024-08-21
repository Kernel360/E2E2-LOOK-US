'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import './LoginPage.scss'
import { API_OAUTH2_URL } from '@/app/_common/constants'

export default function LoginPage() {
    const router = useRouter()
    const [isLoading, setIsLoading] = useState(false)

    const handleGoogleLogin = async () => {
        setIsLoading(true)
        try {
            router.push(`${API_OAUTH2_URL}/google`) // 실제 OAuth2 로그인 URL로 대체
            //
        } catch (error) {
            console.error('Google login failed:', error)
            setIsLoading(false)
        }
    }

    return (
        <div className='login-container'>
            <header className='login-header'>
                <h1 className='login-logo'>LOOK:US</h1>
                <p className='login-description'>
                    당신만의 퍼스널 패션 커뮤니티 서비스
                </p>
            </header>

            <div className='login-content'>
                <button
                    onClick={handleGoogleLogin}
                    disabled={isLoading}
                    className='login-button'
                >
                    {isLoading ? (
                        <svg
                            className='spinner'
                            xmlns='http://www.w3.org/2000/svg'
                            viewBox='0 0 24 24'
                            fill='none'
                            stroke='currentColor'
                            strokeWidth='2'
                            strokeLinecap='round'
                            strokeLinejoin='round'
                        >
                            <circle cx='12' cy='12' r='10' />
                            <path d='M14 2a10 10 0 0 1 0 20' />
                        </svg>
                    ) : (
                        <svg
                            className='google-icon'
                            xmlns='http://www.w3.org/2000/svg'
                            viewBox='0 0 24 24'
                            fill='none'
                            stroke='currentColor'
                            strokeWidth='2'
                            strokeLinecap='round'
                            strokeLinejoin='round'
                        >
                            <path d='M21.35 11.1h-9.86v2.8h5.97c-.54 1.94-2.04 3.2-4.17 3.2-2.56 0-4.66-2.1-4.66-4.67s2.1-4.67 4.66-4.67c1.17 0 2.23.43 3.05 1.14l2.26-2.26c-1.4-1.3-3.26-2.1-5.3-2.1-4.06 0-7.37 3.3-7.37 7.36s3.3 7.36 7.37 7.36c3.97 0 6.85-2.83 6.85-6.81 0-.46-.05-.9-.13-1.33z' />
                        </svg>
                    )}
                    {isLoading ? '로그인 중...' : 'Google로 시작하기'}
                </button>
            </div>
        </div>
    )
}
