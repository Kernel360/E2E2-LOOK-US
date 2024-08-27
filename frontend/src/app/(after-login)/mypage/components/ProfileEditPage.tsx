'use client'
import { useState, useEffect } from 'react'
import {
    myInfoAllFunction,
    myInfoAllResponse,
    updateProfileInfo,
} from '@/app/_api/myPage'
import { API_PUBLIC_URL } from '@/app/_common/constants'
import './profileeditpage.scss'

export default function ProfileEditPage() {
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [nickname, setNickname] = useState('')
    const [gender, setGender] = useState('')
    const [instaId, setInstaId] = useState('')
    const [birth, setBirth] = useState('')

    useEffect(() => {
        async function fetchUserInfo() {
            try {
                const data = await myInfoAllFunction()
                setUserInfo(data)
                setNickname(data.nickname)
                setGender(data.gender)
                setInstaId(data.instaId)
                setBirth(data.birth)
            } catch (error) {
                console.error('Failed to fetch user info:', error)
            }
        }

        fetchUserInfo()
    }, [])

    // const handleProfileSave = async () => {
    //     try {
    //         await updateProfileInfo({
    //             nickname,
    //             gender,
    //             instaId,
    //             birth,
    //         })
    //         alert('프로필이 성공적으로 수정되었습니다!')
    //     } catch (error) {
    //         console.error('Failed to update profile info:', error)
    //     }
    // }

    if (!userInfo) return <div>Loading...</div>

    return (
        <div className='profile-edit-page'>
            <header className='profile-edit-header'>
                <button onClick={() => history.back()} className='back-button'>
                    &times;
                </button>
                <h1>프로필 수정하기</h1>
                <button className='save-button'>완료</button>
            </header>
            <div className='profile-edit-body'>
                <div className='profile-edit-image'>
                    <img
                        src={`${API_PUBLIC_URL}/image/${userInfo.imageId}`}
                        alt='Profile'
                        className='rounded-full'
                    />
                    <button className='change-image-button'>수정</button>
                </div>
                <div className='profile-edit-form'>
                    <label>이름</label>
                    <input
                        type='text'
                        value={nickname}
                        onChange={e => setNickname(e.target.value)}
                    />
                    <label>성별</label>
                    <select
                        value={gender}
                        onChange={e => setGender(e.target.value)}
                    >
                        <option value='WOMAN'>여성</option>
                        <option value='MAN'>남성</option>
                    </select>
                    <label>Instagram ID</label>
                    <input
                        type='text'
                        value={instaId}
                        onChange={e => setInstaId(e.target.value)}
                    />
                    <label>생년월일</label>
                    <input
                        type='date'
                        value={birth}
                        onChange={e => setBirth(e.target.value)}
                    />
                </div>
            </div>
        </div>
    )
}
