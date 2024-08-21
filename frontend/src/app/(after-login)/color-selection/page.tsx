'use client'

import React, { useState, useEffect } from 'react'
import { HexColorPicker } from 'react-colorful'
import { useRouter } from 'next/navigation'

const sections = [
    '아우터',
    '상의',
    '바지',
    '원피스/세트',
    '스커트',
    '신발',
    '가방',
    '주얼리/잡화',
    '모자',
]

const ColorSelectionPage: React.FC = () => {
    const [selectedColors, setSelectedColors] = useState<{
        [key: string]: string
    }>({})
    const [currentSection, setCurrentSection] = useState<string | null>(null)
    const [hoveredSection, setHoveredSection] = useState<string | null>(null)
    const [tempColor, setTempColor] = useState<string>('#ffffff')
    const [isClient, setIsClient] = useState<boolean>(false)
    const router = useRouter()

    useEffect(() => {
        setIsClient(true) // 클라이언트 사이드에서만 렌더링되도록 설정
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
            setCurrentSection(null)
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
        router.back()
    }

    if (!isClient) {
        return null // 서버 사이드에서는 아무것도 렌더링하지 않음
    }

    return (
        <div
            style={{
                width: '100%',
                height: `100vh`,
                margin: '0 auto',
                textAlign: 'center',
                backgroundColor: '#f5f5f5',
                overflowY: 'scroll',
                display: 'flex',
                flexDirection: 'column',
                position: 'relative',
            }}
        >
            {sections.map((section, index) => {
                const isHovered = hoveredSection === section
                const isAbove =
                    window.innerHeight / 2 <
                    (index + 1) * (window.innerHeight / sections.length)

                return (
                    <div
                        key={index}
                        style={{
                            position: 'relative',
                            flex: '1',
                            width: '100%',
                            backgroundColor:
                                selectedColors[section] || '#ffffff',
                            color: selectedColors[section]
                                ? getTextColor(selectedColors[section])
                                : '#000000',
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            cursor: 'pointer',
                            transition: 'all 0.5s ease',
                            fontWeight: isHovered ? '700' : '300',
                            fontSize: isHovered ? '25px' : '16px',
                            height: isHovered ? '1.2em' : '1em',
                            zIndex: isHovered ? 10 : 1,
                        }}
                        onMouseEnter={() => setHoveredSection(section)}
                        onMouseLeave={() => setHoveredSection(null)}
                        onClick={() => setCurrentSection(section)}
                    >
                        {section}
                        {currentSection === section && (
                            <div
                                style={{
                                    position: 'absolute',
                                    left: '50%',
                                    transform: 'translateX(-50%)',
                                    top: isAbove ? 'auto' : '100%',
                                    bottom: isAbove ? '100%' : 'auto',
                                    padding: '10px',
                                    backgroundColor: '#fff',
                                    boxShadow:
                                        '0px 4px 12px rgba(0, 0, 0, 0.1)',
                                    borderRadius: '8px',
                                    zIndex: 20,
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
                                    onClick={applyColor}
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
                    position: 'fixed',
                    width: '100%',
                    top: '50%',
                    transform: 'translateY(-50%)',
                    zIndex: 100,
                    padding: '0 20px',
                }}
            >
                <button
                    onClick={handleGoBack}
                    style={{
                        fontSize: '40px',
                        background: 'none',
                        border: 'none',
                        cursor: 'pointer',
                        outline: 'none',
                    }}
                >
                    ⬅️
                </button>
                <button
                    onClick={() => console.log('Next button clicked')}
                    style={{
                        fontSize: '40px',
                        background: 'none',
                        border: 'none',
                        cursor: 'pointer',
                        outline: 'none',
                    }}
                >
                    ➡️
                </button>
            </div>
        </div>
    )
}

export default ColorSelectionPage
