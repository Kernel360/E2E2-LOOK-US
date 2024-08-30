// src/components/FollowListModal.tsx

'use client'
import { useEffect } from 'react'
import styles from './FollowListModal.module.scss'

interface FollowListModalProps {
    isOpen: boolean
    onClose: () => void
    title: string
    list: any[]
}

export default function FollowListModal({
    isOpen,
    onClose,
    title,
    list,
}: FollowListModalProps) {
    if (!isOpen) return null

    return (
        <div className={styles.modalBackdrop}>
            <div className={styles.modalContainer}>
                <h2>{title}</h2>
                <ul className={styles.list}>
                    {list.map((item, index) => (
                        <li key={index} className={styles.listItem}>
                            <img
                                src={item.profileImage}
                                alt={item.nickname}
                                className={styles.profileImage}
                            />
                            <span className={styles.nickname}>
                                {item.nickname}
                            </span>
                        </li>
                    ))}
                </ul>
                <button className={styles.closeButton} onClick={onClose}>
                    닫기
                </button>
            </div>
        </div>
    )
}
