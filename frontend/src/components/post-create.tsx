import React, { useState, useRef } from 'react'
import { useRouter } from 'next/navigation'
import {
    Box,
    Button,
    Input,
    Text,
    VStack,
    SimpleGrid,
    Tag,
    Textarea,
    HStack,
    IconButton,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalFooter,
    ModalBody,
    ModalCloseButton,
} from '@chakra-ui/react'
import { AddIcon, CloseIcon } from '@chakra-ui/icons'
import { Image as ChakraImage } from '@chakra-ui/react'

interface Category {
    categoryId: number
    categoryContent: string
}

const UploadOOTD = () => {
    const router = useRouter()
    const [step, setStep] = useState(1)
    const [selectedImage, setSelectedImage] = useState<File | null>(null)
    const [croppedImage, setCroppedImage] = useState<string | null>(null)
    const [isCropperOpen, setIsCropperOpen] = useState(false)
    const [cropArea, setCropArea] = useState({
        x: 0,
        y: 0,
        width: 100,
        height: 100,
    }) // 크롭 영역 설정
    const canvasRef = useRef<HTMLCanvasElement | null>(null)
    const [categories, setCategories] = useState<Category[]>([])
    const [selectedCategories, setSelectedCategories] = useState<string[]>([])
    const [content, setContent] = useState('')
    const [hashtags, setHashtags] = useState<string[]>([])
    const [newHashtag, setNewHashtag] = useState('')

    // 카테고리 로딩
    React.useEffect(() => {
        const loadCategories = async () => {
            // 여기에 카테고리 로딩 로직을 추가
        }
        loadCategories()
    }, [])

    const handleImageUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0]
        if (file) {
            setSelectedImage(file)
            setIsCropperOpen(true) // 이미지 업로드 시 크롭 모달
        }
    }

    const handleCrop = () => {
        const canvas = canvasRef.current
        if (canvas && selectedImage) {
            const ctx = canvas.getContext('2d')
            const img = new window.Image() // 또는 const img = new Image();
            img.src = URL.createObjectURL(selectedImage)
            img.onload = () => {
                const { x, y, width, height } = cropArea
                canvas.width = width
                canvas.height = height
                ctx?.drawImage(img, x, y, width, height, 0, 0, width, height)
                setCroppedImage(canvas.toDataURL('image/jpeg'))
                setIsCropperOpen(false) // 크롭 후 모달 닫기
            }
        }
    }

    const handleNextStep = () => {
        if (!croppedImage) {
            alert('이미지를 먼저 업로드하고 자르세요.')
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
        if (!croppedImage || !content || selectedCategories.length === 0) {
            alert('모든 정보를 입력하세요.')
            return
        }

        const userRequest = {
            post_content: content,
            hashtag_content: `#${hashtags.join('#')}`,
            category_content: selectedCategories.join(','),
        }

        const formData = new FormData()

        const response = await fetch(croppedImage!)
        const blob = await response.blob()
        formData.append('image', blob, 'image.jpg')

        const jsonBlob = new Blob([JSON.stringify(userRequest)], {
            type: 'application/json',
        })
        formData.append('userRequest', jsonBlob)

        try {
            // 여기에 서버 요청 로직 추가
            alert('오오티디가 성공적으로 등록되었습니다!')
            router.push('/mypage')
        } catch (error) {
            console.error('Error:', error)
            alert('게시글 등록 중 오류가 발생했습니다.')
        }
    }

    return (
        <VStack spacing={4} align='stretch' p={4}>
            {step === 1 && (
                <VStack spacing={4} align='stretch' p={4}>
                    <Text fontSize='lg' fontWeight='bold'>
                        오오티디 등록하기
                    </Text>
                    <Box
                        borderWidth='1px'
                        borderRadius='lg'
                        overflow='hidden'
                        position='relative'
                        width='100%'
                        paddingBottom='80%'
                        bg='gray.100'
                        display='flex'
                        justifyContent='center'
                        alignItems='center'
                    >
                        {croppedImage ? (
                            <ChakraImage
                                src={croppedImage}
                                alt='Selected Book'
                                objectFit='cover'
                                position='absolute'
                                top='0'
                                left='0'
                                width='100%'
                                height='100%'
                            />
                        ) : (
                            <Text>업로드할 사진을 선택하세요!</Text>
                        )}
                    </Box>
                    <Input type='file' onChange={handleImageUpload} />
                    <Button
                        colorScheme='teal'
                        onClick={handleNextStep}
                        borderRadius='full'
                        boxShadow='md'
                        marginTop='20px'
                    >
                        다음
                    </Button>
                </VStack>
            )}

            {step === 2 && (
                <VStack spacing={4} align='stretch' p={4}>
                    <Text fontSize='lg' fontWeight='bold'>
                        카테고리 선택
                    </Text>
                    <SimpleGrid columns={3} spacing={4}>
                        {categories.map(category => (
                            <Button
                                key={category.categoryId}
                                onClick={() =>
                                    handleCategorySelect(
                                        category.categoryContent,
                                    )
                                }
                                bg={
                                    selectedCategories.includes(
                                        category.categoryContent,
                                    )
                                        ? 'teal.500'
                                        : 'gray.200'
                                }
                                color={
                                    selectedCategories.includes(
                                        category.categoryContent,
                                    )
                                        ? 'white'
                                        : 'black'
                                }
                                borderRadius='full'
                                boxShadow='md'
                            >
                                {category.categoryContent}
                            </Button>
                        ))}
                    </SimpleGrid>
                    <Button
                        colorScheme='teal'
                        onClick={() => setStep(3)}
                        borderRadius='full'
                        boxShadow='md'
                        marginTop='20px'
                    >
                        다음
                    </Button>
                </VStack>
            )}

            {step === 3 && (
                <VStack spacing={4} align='stretch' p={4}>
                    <Text fontSize='lg' fontWeight='bold'>
                        게시글 정보 입력
                    </Text>
                    <Box
                        borderWidth='1px'
                        borderRadius='lg'
                        overflow='hidden'
                        position='relative'
                        width='100%'
                        paddingBottom='80%'
                        bg='gray.100'
                        display='flex'
                        justifyContent='center'
                        alignItems='center'
                    >
                        <ChakraImage
                            src={croppedImage!}
                            alt='Selected Book'
                            objectFit='cover'
                            position='absolute'
                            top='0'
                            left='0'
                            width='100%'
                            height='100%'
                        />
                    </Box>
                    <Textarea
                        placeholder='오오티디에 대한 설명을 입력하세요!'
                        value={content}
                        onChange={e => setContent(e.target.value)}
                        bg='gray.100'
                        borderRadius='lg'
                        marginTop='10px'
                    />
                    <Text fontSize='lg' fontWeight='bold'>
                        해시태그 추가
                    </Text>
                    <HStack>
                        <Input
                            placeholder='#해시태그'
                            value={newHashtag}
                            onChange={e => setNewHashtag(e.target.value)}
                            bg='gray.100'
                            borderRadius='lg'
                        />
                        <IconButton
                            icon={<AddIcon />}
                            onClick={handleAddHashtag}
                            colorScheme='teal'
                            aria-label='hashtag add'
                            borderRadius='full'
                            boxShadow='md'
                        />
                    </HStack>
                    <HStack spacing={2} wrap='wrap'>
                        {hashtags.map((tag, index) => (
                            <Tag
                                key={index}
                                size='lg'
                                colorScheme='teal'
                                borderRadius='full'
                                boxShadow='md'
                            >
                                {tag}
                                <IconButton
                                    icon={<CloseIcon />}
                                    size='xs'
                                    ml={2}
                                    onClick={() => handleRemoveHashtag(index)}
                                    variant='ghost'
                                    colorScheme='teal'
                                    aria-label='removehashtag'
                                    borderRadius='full'
                                />
                            </Tag>
                        ))}
                    </HStack>
                    <Button
                        colorScheme='teal'
                        onClick={handleSubmit}
                        borderRadius='full'
                        boxShadow='md'
                        marginTop='20px'
                    >
                        완료
                    </Button>
                </VStack>
            )}

            <Modal
                isOpen={isCropperOpen}
                onClose={() => setIsCropperOpen(false)}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>이미지 자르기</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>
                        <canvas
                            ref={canvasRef}
                            style={{ width: '100%', height: 'auto' }}
                        />
                    </ModalBody>
                    <ModalFooter>
                        <Button colorScheme='teal' onClick={handleCrop}>
                            자르기
                        </Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </VStack>
    )
}

export default UploadOOTD
