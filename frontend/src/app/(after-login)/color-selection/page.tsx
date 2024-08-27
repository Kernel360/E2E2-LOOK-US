'use client'

import React, { useState, useEffect } from 'react'
import { HexColorPicker } from 'react-colorful'
import { useRouter } from 'next/navigation'
import { API_PUBLIC_URL } from '@/app/_common/constants'

const sections = [
    { name: '아우터', category: 'outer' },
    { name: '상의', category: 'top' },
    { name: '바지', category: 'pants' },
    { name: '원피스/세트', category: 'dress' },
    { name: '스커트', category: 'skirt' },
    { name: '신발', category: 'shoe' },
    { name: '가방', category: 'bag' },
    { name: '주얼리/잡화', category: 'accessory' },
    { name: '모자', category: 'hat' },
]

const ColorSelectionPage: React.FC = () => {
    const [selectedColors, setSelectedColors] = useState<{
        [key: string]: string
    }>({})
    const [currentSection, setCurrentSection] = useState<string | null>(null)
    const [hoveredSection, setHoveredSection] = useState<string | null>(null)
    const [tempColor, setTempColor] = useState<string>('#ffffff')
    const [isClient, setIsClient] = useState<boolean>(false) // 클라이언트 여부 확인 🎀
    const router = useRouter() // Next.js의 useRouter 훅을 사용해요 🎀

    useEffect(() => {
        setIsClient(true) // 컴포넌트가 클라이언트에서 렌더링된 후 설정해요 🎀
    }, [])

    const handleColorChange = (color: string) => {
        setTempColor(color)
    }

    const applyColor = () => {
        if (currentSection) {
            setSelectedColors(prevColors => ({
                ...prevColors,
                [currentSection]: tempColor,
            }))
            setCurrentSection(null) // 선택 후 창을 닫아줌
        }
    }

    const getTextColor = (bgColor: string) => {
        const brightness =
            parseInt(bgColor.substring(1, 3), 16) * 0.299 +
            parseInt(bgColor.substring(3, 5), 16) * 0.587 +
            parseInt(bgColor.substring(5, 7), 16) * 0.114
        return brightness > 150 ? '#000000' : '#FFFFFF'
    }

    const handleGoBack = () => {
        router.back() // 이전 화면으로 이동해요 🎀
    }

    const rgbToIntArray = (color: string) => {
        const r = parseInt(color.slice(1, 3), 16)
        const g = parseInt(color.slice(3, 5), 16)
        const b = parseInt(color.slice(5, 7), 16)
        return [r, g, b]
    }

    const onClickSearchBtn = () => {
        // sendColorAndCategory(tempColor)
        console.log(tempColor)
    }

    const sendColorAndCategory = async (color: string) => {
        const rgbColor = rgbToIntArray(color)
        const data = {
            rgbColor,
        }
        try {
            const response = await fetch(`${API_PUBLIC_URL}/posts`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            })

            if (response.ok) {
                const result = await response.json()
                console.log('검색 결과:', result)
            } else {
                console.error('검색 요청 실패:', response.statusText)
            }
        } catch (error) {
            console.error('검색 중 오류 발생:', error)
        }
    }

    return (
        <div
            style={{
                width: '100%',
                height: `100vh`, // 화면 전체 높이
                margin: '0 auto',
                textAlign: 'center',
                backgroundColor: '#f5f5f5',
                overflowY: 'scroll', // 세로 스크롤 설정
                display: 'flex',
                flexDirection: 'column',
                position: 'relative', // 버튼 위치를 설정하기 위해 추가
            }}
        >
            {sections.map((section, index) => {
                const isHovered = hoveredSection === section.name

                const isAbove =
                    isClient &&
                    window.innerHeight / 2 <
                        (index + 1) * (window.innerHeight / sections.length) // 클라이언트 사이드에서만 실행 🎀

                return (
                    <div
                        key={index}
                        style={{
                            position: 'relative',
                            flex: '1',
                            width: '100%', // Width 고정
                            backgroundColor:
                                selectedColors[section.name] || '#ffffff',
                            color: selectedColors[section.name]
                                ? getTextColor(selectedColors[section.name])
                                : '#000000',
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            cursor: 'pointer',
                            transition: 'all 0.5s ease',
                            fontWeight: isHovered ? '700' : '300',
                            fontSize: isHovered ? '25px' : '16px',
                            height: isHovered ? '1.2em' : '1em', // Hover 시 height 증가
                            zIndex: isHovered ? 10 : 1, // 호버된 섹션이 다른 섹션 위에 오도록 설정
                        }}
                        onMouseEnter={() => setHoveredSection(section.name)}
                        onMouseLeave={() => setHoveredSection(null)}
                        onClick={() => setCurrentSection(section.name)}
                    >
                        {section.name}
                        {currentSection === section.name && (
                            <div
                                style={{
                                    position: 'absolute',
                                    left: '50%',
                                    transform: 'translateX(-50%)',
                                    top: isAbove ? 'auto' : '100%', // 화면 위치에 따라 위/아래에 나타나도록
                                    bottom: isAbove ? '100%' : 'auto', // 화면 위치에 따라 위/아래에 나타나도록
                                    padding: '10px',
                                    backgroundColor: '#fff',
                                    boxShadow:
                                        '0px 4px 12px rgba(0, 0, 0, 0.1)',
                                    borderRadius: '8px',
                                    zIndex: 20, // 컬러 피커가 항상 최상단에 나타나도록 설정
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: 'center',
                                }}
                            >
                                <HexColorPicker
                                    color={tempColor}
                                    onChange={handleColorChange}
                                    style={{
                                        marginBottom: '8px',
                                        width: '200px',
                                        height: '150px',
                                    }}
                                />
                                <button
                                    style={{
                                        width: '80px',
                                        height: '30px',
                                        borderRadius: '4px',
                                        backgroundColor: '#4caf50',
                                        color: '#ffffff',
                                        fontSize: '16px',
                                        fontWeight: 'bold',
                                        border: 'none',
                                        cursor: 'pointer',
                                    }}
                                    onClick={() => {
                                        applyColor()
                                    }}
                                >
                                    확인
                                </button>
                            </div>
                        )}
                    </div>
                )
            })}
            <div
                style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    position: 'fixed', // 화면에 고정되도록 설정
                    width: '100%',
                    top: '50%', // 화면의 중간 높이에 위치하도록 설정
                    transform: 'translateY(-50%)', // 정확히 중앙에 맞추기 위한 transform
                    zIndex: 100, // 버튼이 다른 요소들 위에 겹쳐지도록 z-index를 높임
                    padding: '0 20px', // 버튼과 화면 끝 사이의 여백을 설정
                }}
            >
                <button
                    onClick={handleGoBack}
                    style={{
                        fontSize: '40px', // 버튼 크기를 적당히 키웠어요
                        background: 'none',
                        border: 'none',
                        cursor: 'pointer',
                        outline: 'none', // 포커스 시 나타나는 기본 테두리 제거
                    }}
                >
                    ⬅️
                </button>
                <button
                    onClick={() => onClickSearchBtn()}
                    style={{
                        fontSize: '40px', // 버튼 크기를 적당히 키웠어요
                        background: 'none',
                        border: 'none',
                        cursor: 'pointer',
                        outline: 'none', // 포커스 시 나타나는 기본 테두리 제거
                    }}
                >
                    ➡️
                </button>
            </div>
        </div>
    )
}

export default ColorSelectionPage
