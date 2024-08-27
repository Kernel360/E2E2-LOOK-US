// hooks/useAuth.ts
import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { myInfoAllFunction, myInfoAllResponse } from '@/app/_api/myPage'

export function useAuth() {
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [showModal, setShowModal] = useState(false)
    const router = useRouter()

    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const data = await myInfoAllFunction()
                setUserInfo(data)

                // 유저 정보가 없거나, gender가 null이면 모달을 띄우거나 회원가입 페이지로 리다이렉트
                if (!data || data.gender === null) {
                    setShowModal(true)
                }
            } catch (error) {
                console.error('Failed to fetch user info:', error)
                setShowModal(true) // 에러가 발생해도 모달을 띄움
            }
        }

        fetchUserInfo()
    }, [router])

    const handleCloseModal = () => {
        setShowModal(false)
        router.push('/posts') // 모달 닫을 때 특정 페이지로 이동
    }

    return { userInfo, showModal, handleCloseModal }
}
