// src/components/ProfileModal.tsx

'use client'
import { useState, useEffect } from 'react'
import Image from 'next/image'
import { API_PUBLIC_URL } from '@/app/_common/constants'
import styles from './ProfileModal.module.scss'
import FollowListModal from './FollowListModal'

interface ProfileModalProps {
    isOpen: boolean
    onClose: () => void
    post: any
    onFollowClick: () => void
    isFollowing: boolean
}

export default function ProfileModal({
    isOpen,
    onClose,
    post,
    onFollowClick,
    isFollowing,
}: ProfileModalProps) {
    const [followers, setFollowers] = useState<any[]>([])
    const [followings, setFollowings] = useState<any[]>([])
    const [isFollowersModalOpen, setIsFollowersModalOpen] = useState(false)
    const [isFollowingsModalOpen, setIsFollowingsModalOpen] = useState(false)

    useEffect(() => {
        if (isOpen) {
            fetchFollowersAndFollowings()
        }
    }, [isOpen])

    const fetchFollowersAndFollowings = async () => {
        try {
            const followersResponse = await fetch(
                `${API_PUBLIC_URL}/me/follow/relation?type=followers&nickname=${post.nickname}`,
            )
            const followersData = await followersResponse.json()
            setFollowers(followersData.followers)

            const followingsResponse = await fetch(
                `${API_PUBLIC_URL}/me/follow/relation?type=followings&nickname=${post.nickname}`,
            )
            const followingsData = await followingsResponse.json()
            setFollowings(followingsData.followings)
        } catch (error) {
            console.error('Failed to fetch followers/followings data:', error)
        }
    }

    if (!isOpen) return null

    return (
        <div className={styles.modalBackdrop}>
            <div className={styles.modalContainer}>
                <div className={styles.profileSection}>
                    <Image
                        src={`${API_PUBLIC_URL}/image/${post.profileImageLocationId}`}
                        alt='profile'
                        width={100}
                        height={100}
                        className={styles.profileImage}
                    />
                    <span className={styles.username}>{post.nickname}</span>
                    <button
                        className={
                            isFollowing
                                ? styles.followingButton
                                : styles.followButton
                        }
                        onClick={onFollowClick}
                    >
                        {isFollowing ? '팔로잉' : '팔로우'}
                    </button>
                </div>
                <div className={styles.statsSection}>
                    <span
                        onClick={() => setIsFollowersModalOpen(true)}
                        className='clickable'
                    >
                        팔로워 0명
                    </span>
                    <span
                        onClick={() => setIsFollowingsModalOpen(true)}
                        className='clickable'
                    >
                        팔로잉 0명
                    </span>
                </div>
                <button className={styles.closeButton} onClick={onClose}>
                    닫기
                </button>

                {/* 팔로워/팔로잉 모달 */}
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
        </div>
    )
}
