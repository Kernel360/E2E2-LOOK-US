import React, { useState, useRef } from 'react'
import { useRouter } from 'next/navigation'
import { AddIcon, CloseIcon } from '@chakra-ui/icons'
import { API_PRIVATE_URL, API_PUBLIC_URL } from '@/app/_common/constants'
import { createPost } from '@/app/_api/post'
import styles from './post-create.module.scss'
import Image from 'next/image'
import Link from 'next/link'

interface Category {
    categoryId: number
    categoryContent: string
}

const UploadOOTD = () => {
    const router = useRouter()
    const [step, setStep] = useState(1)
    const [selectedImage, setSelectedImage] = useState<File | null>(null)
    const canvasRef = useRef<HTMLCanvasElement | null>(null)
    const [categories, setCategories] = useState<Category[]>([])
    const [selectedCategories, setSelectedCategories] = useState<string[]>([])
    const [content, setContent] = useState('')
    const [hashtags, setHashtags] = useState<string[]>([])
    const [newHashtag, setNewHashtag] = useState('')

    // 카테고리 로딩
    React.useEffect(() => {
        const loadCategories = async () => {
            const response = await fetch(
                `${API_PUBLIC_URL}` + `/posts/categoryAll`,
            )
            const data: Category[] = await response.json()
            setCategories(data)
        }
        loadCategories()
    }, [])

    const handleImageUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0]
        if (file) {
            setSelectedImage(file)
            handleCrop(file)
        }
    }

    const handleCrop = (imageFile: File) => {
        const canvas = canvasRef.current
        if (canvas) {
            const ctx = canvas.getContext('2d')
            const img = new window.Image()
            img.src = URL.createObjectURL(imageFile)
            img.onload = () => {
                // 4:5 비율로 크롭
                const cropWidth = img.width
                const cropHeight = (img.width * 5) / 4
                canvas.width = cropWidth
                canvas.height = cropHeight
                ctx?.drawImage(
                    img,
                    0,
                    0,
                    cropWidth,
                    cropHeight,
                    0,
                    0,
                    cropWidth,
                    cropHeight,
                )
            }
        }
    }

    const handleNextStep = () => {
        if (!selectedImage) {
            alert('이미지를 먼저 업로드하세요.')
            return
        }
        setStep(2)
    }

    const handleCategorySelect = (categoryContent: string) => {
        setSelectedCategories(prevCategories =>
            prevCategories.includes(categoryContent)
                ? prevCategories.filter(c => c !== categoryContent)
                : [...prevCategories, categoryContent],
        )
    }

    const handleAddHashtag = () => {
        if (newHashtag.trim()) {
            setHashtags([...hashtags, newHashtag.trim()])
            setNewHashtag('')
        }
    }

    const handleRemoveHashtag = (index: number) => {
        setHashtags(hashtags.filter((_, i) => i !== index))
    }

    const handleSubmit = async () => {
        if (!selectedImage || !content || selectedCategories.length === 0) {
            alert('모든 정보를 입력하세요.')
            return
        }

        const userRequest = {
            post_content: content,
            hashtag_content: `#${hashtags.join('#')}`,
            category_content: selectedCategories.join(','),
        }

        const formData = new FormData()

        formData.append('image', selectedImage)
        const jsonBlob = new Blob([JSON.stringify(userRequest)], {
            type: 'application/json',
        })
        formData.append('userRequest', jsonBlob)

        try {
            // 여기에 서버 요청 로직 추가
            await createPost(formData)
            alert('오오티디가 성공적으로 등록되었습니다!')
            router.push('/mypage')
        } catch (error) {
            console.error('Error:', error)
            alert('게시글 등록 중 오류가 발생했습니다.')
        }
    }

    const renderStep = () => {
        switch (step) {
            case 1: // 이미지 업로드 단계
                return (
                    <div className={styles.container}>
                        <div className={styles.logoWrapper}>
                            <div className={styles.arrowLeft}>
                                <Link href='/posts'>
                                    <Image
                                        src='/images/arrow-left.png'
                                        alt='Back'
                                        width={35}
                                        height={35}
                                    />
                                </Link>
                            </div>
                            <Image
                                src='/images/LOOKUSlogo.png'
                                alt='LOOK:US'
                                width={171}
                                height={36}
                            />
                        </div>
                        <div
                            className={styles.uploadBox}
                            onClick={() =>
                                document.getElementById('imageInput')?.click()
                            }
                        >
                            {selectedImage ? (
                                <img
                                    src={URL.createObjectURL(selectedImage)}
                                    alt='Selected'
                                />
                            ) : (
                                <Image
                                    src='/images/uploadImg.png'
                                    alt='Upload Placeholder'
                                    width={70}
                                    height={58}
                                />
                            )}
                        </div>
                        <input
                            id='imageInput'
                            type='file'
                            onChange={handleImageUpload}
                            style={{ display: 'none' }}
                        />
                        <div
                            className={styles.nextButton}
                            onClick={handleNextStep}
                        >
                            <span>다음으로</span>
                        </div>
                    </div>
                )

            case 2: // 카테고리 선택 단계
                return (
                    <div className={styles.container}>
                        <div className={styles.logoWrapper}>
                            <div className={styles.arrowLeft}>
                                <Link href='/posts'>
                                    <Image
                                        src='/images/arrow-left.png'
                                        alt='Back'
                                        width={35}
                                        height={35}
                                    />
                                </Link>
                            </div>
                            <Image
                                src='/images/LOOKUSlogo.png'
                                alt='LOOK:US'
                                width={171}
                                height={36}
                            />
                        </div>
                        <div className={styles.imageBox}>
                            <img
                                src={URL.createObjectURL(selectedImage!)}
                                alt='Selected'
                            />
                        </div>
                        <div className={styles.categoryWrapper}>
                            {categories.map(category => (
                                <div
                                    key={category.categoryId}
                                    className={`${styles.categoryButton} ${selectedCategories.includes(category.categoryContent) ? styles.selected : ''}`}
                                    onClick={() =>
                                        handleCategorySelect(
                                            category.categoryContent,
                                        )
                                    }
                                >
                                    {category.categoryContent}
                                </div>
                            ))}
                        </div>
                        <div
                            className={styles.nextButton}
                            onClick={() => setStep(3)}
                        >
                            <span>다음으로</span>
                        </div>
                    </div>
                )

            case 3: // 게시글 정보 입력 단계
                return (
                    <div className={styles.container}>
                        <div className={styles.logoWrapper}>
                            <div className={styles.arrowLeft}>
                                <Link href='/posts'>
                                    <Image
                                        src='/images/arrow-left.png'
                                        alt='Back'
                                        width={35}
                                        height={35}
                                    />
                                </Link>
                            </div>
                            <Image
                                src='/images/LOOKUSlogo.png'
                                alt='LOOK:US'
                                width={171}
                                height={36}
                            />
                        </div>
                        <div className={styles.imageBox}>
                            <img
                                src={URL.createObjectURL(selectedImage!)}
                                alt='Selected'
                            />
                        </div>
                        <div className={styles.inputBox}>
                            <textarea
                                placeholder='포스트를 입력해주세요'
                                value={content}
                                onChange={e => setContent(e.target.value)}
                            />
                        </div>
                        <div className={styles.hashtagWrapper}>
                            <input
                                placeholder='#해시태그'
                                value={newHashtag}
                                onChange={e => setNewHashtag(e.target.value)}
                                onKeyPress={e => {
                                    if (e.key === 'Enter') {
                                        handleAddHashtag()
                                    }
                                }}
                            />
                            {hashtags.map((tag, index) => (
                                <div
                                    key={index}
                                    className={styles.hashtag}
                                    onClick={() => handleRemoveHashtag(index)}
                                >
                                    {tag}
                                </div>
                            ))}
                        </div>
                        <div
                            className={styles.submitButton}
                            onClick={handleSubmit}
                        >
                            <span>작성 완료</span>
                        </div>
                    </div>
                )

            default:
                return null
        }
    }

    return <div className={styles.container}>{renderStep()}</div>
}

export default UploadOOTD
