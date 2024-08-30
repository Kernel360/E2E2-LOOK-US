import React, { useEffect, useState } from 'react'
import styles from './ColorPickerModal.module.scss'
import { fetchPopularColor, PopularColor } from '@/app/_api/category'
import useDebounce from '@/app/hooks/debounce'

interface ColorPickerModalProps {
    initialColor?: string
    onComplete: (color: number[]) => void
    onClose: () => void
    onCategoryOnlySelect: () => void // 카테고리만 선택할 때 호출되는 함수
    onColorChange: (color: number[]) => void // New prop to handle color change
    onResetCategory: () => void // 카테고리 선택 초기화 함수 추가
}

// RGB 값을 HEX 값으로 변환하는 함수
const rgbToHex = (r: number, g: number, b: number): string => {
    const toHex = (component: number) => component.toString(16).padStart(2, '0')
    return `#${toHex(r)}${toHex(g)}${toHex(b)}`
}
// 문자열 색상을 RGB 배열로 변환하는 함수
const parseColorToRgb = (color: string): number[] => {
    let r = 0,
        g = 0,
        b = 0

    if (color.startsWith('rgb')) {
        const match = color.match(/\d+/g)
        if (match) {
            r = parseInt(match[0])
            g = parseInt(match[1])
            b = parseInt(match[2])
        }
    } else if (color.startsWith('hsl')) {
        const match = color.match(/\d+/g)
        if (match) {
            const h = parseInt(match[0])
            const s = parseInt(match[1])
            const l = parseInt(match[2])
            ;[r, g, b] = hslToRgb(h, s, l)
        }
    } else if (color.startsWith('#')) {
        r = parseInt(color.substring(1, 3), 16)
        g = parseInt(color.substring(3, 5), 16)
        b = parseInt(color.substring(5, 7), 16)
    }

    return [r, g, b]
}
// HSL을 RGB로 변환하는 함수
function hslToRgb(h: number, s: number, l: number): [number, number, number] {
    s /= 100
    l /= 100
    const k = (n: number) => (n + h / 30) % 12
    const a = s * Math.min(l, 1 - l)
    const f = (n: number) =>
        l - a * Math.max(-1, Math.min(k(n) - 3, Math.min(9 - k(n), 1)))
    return [
        Math.round(f(0) * 255),
        Math.round(f(8) * 255),
        Math.round(f(4) * 255),
    ]
}
const ColorPickerModal: React.FC<ColorPickerModalProps> = ({
    initialColor = '',
    onComplete,
    onClose,
    onCategoryOnlySelect, // 카테고리만 선택 함수 추가
    onColorChange,

    onResetCategory,
}) => {
    const [selectedColor, setSelectedColor] = useState<string>(initialColor)
    const [sliderColor, setSliderColor] = useState<string>(initialColor)
    const [trendColors, setTrendColors] = useState<string[]>([]) // trendColors 상태로 관리
    const debouncedColor = useDebounce(selectedColor, 300) // Apply debounce

    useEffect(() => {
        const loadPopularColors = async () => {
            try {
                const colors: PopularColor[] = await fetchPopularColor()
                const hexColors = colors.map(color =>
                    rgbToHex(color.r, color.g, color.b),
                )
                setTrendColors(hexColors)
            } catch (error) {
                console.error('Failed to fetch popular colors:', error)
            }
        }

        loadPopularColors()
    }, [])
    // useEffect(() => {
    //     if (debouncedColor) {
    //         const rgbColor = parseColorToRgb(debouncedColor)
    //         onColorChange(rgbColor) // Trigger real-time updates
    //     }
    // }, [debouncedColor, onColorChange])
    const handleColorSelect = (color: string) => {
        setSelectedColor(color)
        setSliderColor('')
    }

    const handleSliderChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value
        const hue = parseInt(value)
        const color = `hsl(${hue}, 100%, 50%)`
        setSelectedColor(color)
        setSliderColor(color)
    }

    const handleComplete = () => {
        const rgbColor = parseColorToRgb(selectedColor)
        onComplete(rgbColor)
    }

    const handleClose = () => {
        onResetCategory() // 카테고리 선택 초기화
        onClose() // 모달 닫기
    }
    const handleCategoryOnlySelect = () => {
        // 카테고리만 선택 시 색상 초기화
        onColorChange([255, 255, 255]) // 완전히 흰색으로 설정 (혹은 초기화 값 설정)
        setSelectedColor('') // 선택된 색상 초기화
        setSliderColor('') // 슬라이더 색상도 초기화
        onCategoryOnlySelect() // 카테고리만 선택
    }
    const sliderHue = sliderColor
        ? parseInt(sliderColor.match(/\d+/)?.[0] || '180')
        : 180
    const sliderPosition = (sliderHue / 360) * 100

    return (
        <div className={styles.modalBackdrop}>
            <div className={styles.modalContainer}>
                <div className={styles.modalHeader}>
                    <div className={styles.modalTitle}>색상 선택하기</div>
                    <div className={styles.closeButton} onClick={handleClose}>
                        &times;
                    </div>
                </div>
                <div className={styles.sliderContainer}>
                    <input
                        type='range'
                        min='0'
                        max='360'
                        value={sliderHue}
                        onChange={handleSliderChange}
                        className={styles.slider}
                    />
                    <div
                        className={styles.sliderIndicator}
                        style={{ left: `${sliderPosition}%` }}
                    >
                        <div
                            className={styles.indicatorColor}
                            style={{ backgroundColor: selectedColor }}
                        ></div>
                    </div>
                </div>
                <div className={styles.modalTitle}>오늘의 트렌드 컬러는?</div>
                <div className={styles.swatches}>
                    {trendColors.map((color, index) => (
                        <div
                            key={index}
                            className={`${styles.swatch} ${
                                selectedColor === color ? styles.selected : ''
                            }`}
                            style={{ backgroundColor: color }}
                            onClick={() => handleColorSelect(color)}
                        ></div>
                    ))}
                </div>
                <div className={styles.buttonsContainer}>
                    <div
                        className={styles.button}
                        onClick={handleCategoryOnlySelect} // 카테고리만 선택
                    >
                        카테고리만 선택
                    </div>
                    <div className={styles.button} onClick={handleComplete}>
                        완료
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ColorPickerModal
