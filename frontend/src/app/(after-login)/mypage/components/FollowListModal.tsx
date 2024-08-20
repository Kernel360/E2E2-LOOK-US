import React from 'react'
import { API_PUBLIC_URL } from '@/app/_common/constants'
import './followlistmodal.scss' // 스타일 파일 임포트

interface FollowListModalProps {
    isOpen: boolean
    onClose: () => void
    title: string
    list: {
        nickname: string
        followersCount: number
        profileImageId: number | null
    }[]
}

const FollowListModal: React.FC<FollowListModalProps> = ({
    isOpen,
    onClose,
    title,
    list,
}) => {
    if (!isOpen) return null

    return (
        <div className='modal-overlay'>
            <div className='modal-content'>
                <h2 className='modal-title'>{title}</h2>
                <ul className='modal-list'>
                    {list.map((follower, index) => (
                        <li key={index} className='modal-list-item'>
                            <img
                                src={
                                    follower.profileImageId
                                        ? `${API_PUBLIC_URL}/image/${follower.profileImageId}`
                                        : '/images/default-profile.png'
                                }
                                alt={follower.nickname}
                                className='profile-image-small'
                            />
                            <div className='follower-info'>
                                <p className='nickname'>{follower.nickname}</p>
                                <p className='followers-count'>
                                    팔로워 {follower.followersCount}명
                                </p>
                            </div>
                        </li>
                    ))}
                </ul>
                <div className='modal-actions'>
                    <button onClick={onClose} className='modal-close-button'>
                        닫기
                    </button>
                </div>
            </div>
        </div>
    )
}

export default FollowListModal
