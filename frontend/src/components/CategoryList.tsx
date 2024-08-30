import React, { useState } from 'react'
import ColorPickerModal from './ColorPickerModal'
import styles from './CategoryList.module.scss'
import { CategoryEntity } from '@/app/_api/category'

interface CategoryListProps {
    categories: CategoryEntity[]
    onSelectCategory: (categoryId: number) => void // string -> number로 변경
    onSelectCategoryAndColor: (categoryId: number, color: number[]) => void // string -> number로 변경
    onColorChange: (color: number[]) => void
}

export default function CategoryList({
    categories,
    onSelectCategory,
    onSelectCategoryAndColor,
    onColorChange,
}: CategoryListProps) {
    const [selectedCategoryId, setSelectedCategoryId] = useState<number | null>(
        null,
    )
    const [modalVisible, setModalVisible] = useState<boolean>(false)
    const [selectedCategoryColor, setSelectedCategoryColor] = useState<{
        [key: string]: string
    }>({})

    const handleCategoryClick = (categoryId: number) => {
        // string -> number로 변경
        if (selectedCategoryId === categoryId) {
            setSelectedCategoryId(null)
            setSelectedCategoryColor(prev => {
                const newColors = { ...prev }
                delete newColors[categoryId]
                return newColors
            })
            onSelectCategory(0) // 선택 해제 시 기본값으로 (0)을 넘겨줌
        } else {
            setSelectedCategoryId(categoryId)
            setModalVisible(true)
        }
    }

    const handleColorComplete = (rgbColor: number[]) => {
        if (selectedCategoryId !== null) {
            const colorString = `rgb(${rgbColor.join(', ')})`
            setSelectedCategoryColor(prev => {
                const newState = {
                    ...prev,
                    [selectedCategoryId]: colorString,
                }
                console.log('Updated State:', newState) // 업데이트 후 상태 확인
                return newState
            })
            onSelectCategoryAndColor(selectedCategoryId, rgbColor)
        }
        setModalVisible(false)
    }

    const handleCategoryOnlySelect = () => {
        if (selectedCategoryId) {
            setSelectedCategoryColor(prev => ({
                ...prev,
                [selectedCategoryId]: '',
            }))
            onSelectCategory(selectedCategoryId)
            onColorChange([255, 255, 255])
        }
        setModalVisible(false)
    }

    const handleCloseModal = () => {
        setSelectedCategoryId(null)
        setModalVisible(false)
    }

    const handleColorChange = (rgbColor: number[]) => {
        if (selectedCategoryId) {
            onColorChange(rgbColor)
        }
    }

    return (
        <div className={styles.categoryList}>
            <button
                className={`${styles.categoryButton} ${
                    selectedCategoryId === 0 ? styles.selected : ''
                }`}
                style={{
                    backgroundColor: selectedCategoryColor[0] || 'transparent',
                    borderColor: selectedCategoryColor[0]
                        ? 'transparent'
                        : selectedCategoryId === 0
                          ? '#000'
                          : '#D3D3D3',
                    color: selectedCategoryColor[0]
                        ? isBrightColor(selectedCategoryColor[0])
                            ? '#000000'
                            : '#FFFFFF'
                        : selectedCategoryId === 0
                          ? '#333'
                          : '#898989',
                }}
                onClick={() => handleCategoryClick(0)} // 전체 버튼 클릭 시 categoryId를 0으로 설정
            >
                전체
            </button>
            {categories.map(category => (
                <button
                    key={category.categoryId}
                    className={`${styles.categoryButton} ${
                        selectedCategoryId === category.categoryId
                            ? selectedCategoryColor[category.categoryId]
                                ? styles.colored
                                : styles.selected
                            : ''
                    }`}
                    style={{
                        backgroundColor:
                            selectedCategoryColor[category.categoryId] ||
                            'transparent',
                        borderColor: selectedCategoryColor[category.categoryId]
                            ? 'transparent'
                            : selectedCategoryId === category.categoryId
                              ? '#000'
                              : '#D3D3D3',
                        color: selectedCategoryColor[category.categoryId]
                            ? isBrightColor(
                                  selectedCategoryColor[category.categoryId],
                              )
                                ? '#000000'
                                : '#FFFFFF'
                            : selectedCategoryId === category.categoryId
                              ? '#333'
                              : '#898989',
                    }}
                    onClick={() => handleCategoryClick(category.categoryId)}
                >
                    {category.categoryContent}
                </button>
            ))}
            {modalVisible && (
                <ColorPickerModal
                    onComplete={handleColorComplete}
                    onClose={handleCloseModal}
                    onCategoryOnlySelect={handleCategoryOnlySelect}
                    onColorChange={handleColorChange}
                    onResetCategory={handleCloseModal}
                />
            )}
        </div>
    )
}

function isBrightColor(color: string): boolean {
    let r: number, g: number, b: number
    if (color.startsWith('rgb')) {
        const match = color.match(/\d+/g)
        r = parseInt(match![0], 10)
        g = parseInt(match![1], 10)
        b = parseInt(match![2], 10)
    } else if (color.startsWith('hsl')) {
        const match = color.match(/\d+/g)
        const h = parseInt(match![0], 10)
        const s = parseInt(match![1], 10)
        const l = parseInt(match![2], 10)
        ;[r, g, b] = hslToRgb(h, s, l)
    } else {
        r = parseInt(color.substring(1, 3), 16)
        g = parseInt(color.substring(3, 5), 16)
        b = parseInt(color.substring(5, 7), 16)
    }

    const brightness = (r * 299 + g * 587 + b * 114) / 1000
    return brightness > 186
}

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
