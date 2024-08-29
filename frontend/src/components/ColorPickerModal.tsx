import React, { useState } from 'react'
import styles from './ColorPickerModal.module.scss'

interface ColorPickerModalProps {
    initialColor?: string
    onComplete: (color: string) => void
    onClose: () => void
    onCategoryOnlySelect: () => void // 카테고리만 선택할 때 호출되는 함수
    onResetCategory: () => void // 카테고리 선택 초기화 함수 추가
}

const ColorPickerModal: React.FC<ColorPickerModalProps> = ({
    initialColor = '',
    onComplete,
    onClose,
    onCategoryOnlySelect, // 카테고리만 선택 함수 추가
    onResetCategory,
}) => {
    const [selectedColor, setSelectedColor] = useState<string>(initialColor)
    const [sliderColor, setSliderColor] = useState<string>(initialColor)

    const trendColors = [
        '#EF4444',
        '#F97316',
        '#FACC15',
        '#2DD4BF',
        '#6366F1',
        '#EC4899',
        '#F43F5E',
        '#D946EF',
        '#0EA5E9',
        '#84CC16',
    ]

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
        onComplete(selectedColor)
    }

    const handleClose = () => {
        onResetCategory() // 카테고리 선택 초기화
        onClose() // 모달 닫기
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
                        onClick={onCategoryOnlySelect} // 카테고리만 선택
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
