'use client'

// https://github.com/advanced-cropper/react-advanced-cropper/tree/master
// https://advanced-cropper.github.io/react-advanced-cropper/docs/tutorials/image-editor/
// https://advanced-cropper.github.io/react-advanced-cropper/docs/guides/cropper-types#fixed-cropper

/**
 * TODO: 각 이미지 편집 옵션마다 슬라이더 값을 저장하고, 이를
 *       숫자로 보여주는 것이 좋겠다.
 */

import { getMimeType } from 'advanced-cropper/extensions/mimes'
import React, { useState, useRef, ChangeEvent } from 'react'
import cn from 'classnames'
import {
    Cropper,
    CropperRef,
    CropperPreview,
    CropperPreviewRef,
    FixedCropper,
    ImageRestriction,
    FixedCropperRef,
} from 'react-advanced-cropper'
import { Navigation } from './core/Navigation'
import { Undo } from 'lucide-react'
import './image-editor.scss'
import { Slider } from '../ui/slider'
import { Button } from '../ui/button'
import { AdjustableCropperBackground } from './core/AdjustableCropperBackground'
import 'react-advanced-cropper/dist/style.css'
import 'react-advanced-cropper/dist/themes/corners.css'
import { Image } from '../post-create'
import { IMAGE_ASPECT_RATIO } from '@/app/_common/constants'

// The polyfill for Safari browser. The dynamic require is needed to work with SSR
if (typeof window !== 'undefined') {
    require('context-filter-polyfill')
}

// 4:5 Aspect Ratio for Fassion Photo
const CROP_HEIGHT = 700
const CROP_WIDTH = CROP_HEIGHT * IMAGE_ASPECT_RATIO

export const ImageEditor = ({
    setImage,
}: {
    setImage: (image: Image) => void
}) => {
    const cropperRef = useRef<FixedCropperRef>(null)
    const previewRef = useRef<CropperPreviewRef>(null)

    const [src, setSrc] = useState('')
    const [type, setType] = useState<string | undefined>()

    // 'brightness' | 'hue' | 'saturation' | 'contrast' | 'crop'
    const [mode, setMode] = useState('')

    const [adjustments, setAdjustments] = useState({
        brightness: 0,
        hue: 0,
        saturation: 0,
        contrast: 0,
    })

    const onChangeValue = (value: number) => {
        if (mode && mode in adjustments) {
            setAdjustments(previousValue => ({
                ...previousValue,
                [mode]: value,
            }))
        }
    }

    const onReset = () => {
        setMode('crop')
        setAdjustments({
            brightness: 0,
            hue: 0,
            saturation: 0,
            contrast: 0,
        })
    }

    const onLoadImage = (event: ChangeEvent<HTMLInputElement>) => {
        // Reference to the DOM input element
        const { files } = event.target

        // Ensure that you have a file before attempting to read it
        if (files && files[0]) {
            // Create the blob link to the file to optimize performance:
            const blob = URL.createObjectURL(files[0])

            // Remember the fallback type:
            const typeFallback = files[0].type

            // Create a new FileReader to read this image binary data
            const reader = new FileReader()

            // Define a callback function to run, when FileReader finishes its job
            reader.onload = e => {
                // Note: arrow function used here, so that "this.image" refers to the image of Vue component
                // Read image as base64 and set it as src:
                setSrc(blob)

                setMode('crop')

                // Determine the image type to preserve it during the extracting the image from canvas:
                setType(getMimeType(e.target?.result, typeFallback))
            }
            // Start the reader job - read file as a data url (base64 format) and get the real file type
            reader.readAsArrayBuffer(files[0])
        }
        // Clear the event target value to give the possibility to upload the same image:
        event.target.value = ''
    }

    // Prepare to sent image to server
    // - use setImage callback
    const onEditDone = () => {
        if (!cropperRef.current) return

        cropperRef.current.getCanvas()?.toBlob(blob => {
            if (blob) {
                setImage({ blob: blob })
            }
        }, 'image/jpeg')
    }

    const onUpdate = (cropper: CropperRef) => {
        previewRef.current?.update(cropper)
    }

    const changed = Object.values(adjustments).some(el => Math.floor(el * 100))

    const cropperEnabled = mode === 'crop'

    return (
        <div className={'image-editor'}>
            <div className='image-editor__cropper'>
                <FixedCropper
                    src={src}
                    ref={cropperRef}
                    stencilSize={{
                        width: CROP_WIDTH,
                        height: CROP_HEIGHT,
                    }}
                    stencilProps={{
                        grid: true,
                        movable: false,
                        resizable: false,
                        lines: cropperEnabled,
                        handlers: cropperEnabled,
                        overlayClassName: cn(
                            'image-editor__cropper-overlay',
                            !cropperEnabled &&
                                'image-editor__cropper-overlay--faded',
                        ),
                    }}
                    imageRestriction={ImageRestriction.stencil}
                    backgroundWrapperProps={{
                        scaleImage: cropperEnabled,
                        moveImage: cropperEnabled,
                    }}
                    backgroundComponent={AdjustableCropperBackground}
                    backgroundProps={adjustments}
                    onUpdate={onUpdate}
                />
                {mode && mode !== 'crop' && (
                    <Slider
                        className='image-editor__slider'
                        defaultValue={[
                            adjustments[mode as keyof typeof adjustments],
                        ]}
                        onValueChange={i => onChangeValue(i[0])}
                    />
                )}
                <Button
                    className={cn(
                        'image-editor__reset-button',
                        !changed && 'image-editor__reset-button--hidden',
                    )}
                    onClick={onReset}
                >
                    <Undo />
                </Button>
            </div>
            <Navigation
                mode={mode}
                onChange={setMode}
                onLoadImage={onLoadImage}
                onEditDone={onEditDone}
            />
        </div>
    )
}
