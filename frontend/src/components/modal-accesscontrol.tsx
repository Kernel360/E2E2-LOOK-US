import React from 'react'
import Link from 'next/link'
import styles from './modal-accesscontrol.module.scss'

interface ModalProps {
    show: boolean
    onClose: () => void
}

const Modal: React.FC<ModalProps> = ({ show, onClose }) => {
    if (!show) return null

    return (
        <div className={styles['modal-backdrop']}>
            <div className={styles['modal-container']}>
                <p className={styles['modal-text']}>
                    회원가입하면 더 많은 정보를 얻을 수 있어요 :)
                </p>
                <div className={styles['modal-button-container']}>
                    <Link href='/login' className={styles['modal-button']}>
                        좋았어. 바로 가보자고
                    </Link>
                    <Link href='/posts' className={styles['modal-button']}>
                        그냥 글이나 볼래요
                    </Link>
                </div>
                <button onClick={onClose} className={styles['modal-cancel']}>
                    취소
                </button>
            </div>
        </div>
    )
}

export default Modal
