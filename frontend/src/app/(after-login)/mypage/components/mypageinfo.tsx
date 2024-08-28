'use client'
import { useEffect, useState } from 'react'
import {
    updateProfileImg,
    myInfoAllFunction,
    myInfoAllResponse,
} from '@/app/_api/myPage'
import { API_PUBLIC_URL } from '@/app/_common/constants'
import FollowListModal from './FollowListModal' // 팔로워/팔로잉 모달 임포트
import './mypageinfo.scss' // 스타일 파일 임포트
import Link from 'next/link'

export default function MyPageInfoComponent() {
    const [userInfo, setUserInfo] = useState<myInfoAllResponse | null>(null)
    const [isFollowersModalOpen, setIsFollowersModalOpen] = useState(false)
    const [isFollowingsModalOpen, setIsFollowingsModalOpen] = useState(false)
    const [followers, setFollowers] = useState([])
    const [followings, setFollowings] = useState([])
    // const [isUserInfoLoaded, setIsUserInfoLoaded] = useState(false);

    useEffect(() => {
        (async () => {
            try {
                const data = await myInfoAllFunction()
                setUserInfo(data)

                const followersResponse = await fetch(
                    `${API_PUBLIC_URL}/me/follow/relation?type=followers&nickname=${data.nickname}`,
                )
                const followersData = await followersResponse.json()
                setFollowers(followersData.followers)

                const followingsResponse = await fetch(
                    `${API_PUBLIC_URL}/me/follow/relation?type=followings&nickname=${data.nickname}`,
                )
                const followingsData = await followingsResponse.json()
                setFollowings(followingsData.followers)
            } catch (error) {
                console.error('Failed to fetch user info:', error)
            }
        })(/* IIFE */);

    }, [])

    const handleProfileImageSave = async (file: File) => {
        try {
            await updateProfileImg(file)
            const updatedUserInfo = await myInfoAllFunction()
            setUserInfo(updatedUserInfo)
        } catch (error) {
            console.error('Failed to update profile image:', error)
        }
    }

    if (!userInfo) return <div>Loading...</div>

    return (
        <div className='mypage-info'>
            <div className='mypage-info-header'>
                {userInfo ? (
                    <img
                        src={`${API_PUBLIC_URL}/image/${userInfo.imageLocationId}`}
                        alt='Avatar'
                        className='rounded-full profile-image'
                    />
                ) : (
                    <img
                        src={`https://t4.ftcdn.net/jpg/05/49/98/39/360_F_549983970_bRCkYfk0P6PP5fKbMhZMIb07mCJ6esXL.jpg`}
                        alt='Avatar'
                        className='rounded-full profile-image'
                    />
                )}
                <div className='user-details'>
                    <h2 className='mypage-username'>{userInfo.nickname}</h2>
                    <a
                        href={`https://www.instagram.com/${userInfo.instaId}/`}
                        target='_blank'
                        rel='noopener noreferrer'
                        className='mypage-instagram'
                    >
                        @{userInfo.instaId}
                    </a>
                </div>
                <div className='mypage-actions'>
                    <Link href='/mypage/edit' className='profile-edit-button'>
                        <button>프로필 편집</button>
                    </Link>
                    <button className='profile-share-button'>
                        프로필 공유
                    </button>
                </div>
            </div>
            <div className='mypage-stats'>
                <span>{userInfo.postNum} 게시물</span>
                <span
                    onClick={() => setIsFollowersModalOpen(true)}
                    className='clickable'
                >
                    {followers.length} 팔로워
                </span>
                <span
                    onClick={() => setIsFollowingsModalOpen(true)}
                    className='clickable'
                >
                    {followings.length} 팔로잉
                </span>
            </div>

            <FollowListModal
                isOpen={isFollowersModalOpen}
                onClose={() => setIsFollowersModalOpen(false)}
                title='팔로워 목록'
                list={followers}
            />

            <FollowListModal
                isOpen={isFollowingsModalOpen}
                onClose={() => setIsFollowingsModalOpen(false)}
                title='팔로잉 목록'
                list={followings}
            />
        </div>
    )
}