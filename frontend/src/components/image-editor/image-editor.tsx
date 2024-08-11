"use client"

// https://github.com/advanced-cropper/react-advanced-cropper/tree/master
// https://advanced-cropper.github.io/react-advanced-cropper/docs/tutorials/image-editor/
// https://advanced-cropper.github.io/react-advanced-cropper/docs/guides/cropper-types#fixed-cropper

/**  
 * TODO: 각 이미지 편집 옵션마다 슬라이더 값을 저장하고, 이를
 *       숫자로 보여주는 것이 좋겠다.
*/

import React, { useState, useRef } from 'react';
import cn from 'classnames';
import { Cropper, CropperRef, CropperPreview, CropperPreviewRef, FixedCropper, ImageRestriction, FixedCropperRef } from 'react-advanced-cropper';
import { Navigation } from './core/Navigation';
import { Grid, Undo } from 'lucide-react';
import './image-editor.scss';
import { Slider } from '../ui/slider';
import { Button } from '../ui/button';
import { AdjustablePreviewBackground } from './core/AdjustablePreviewBackground';
import { AdjustableCropperBackground } from './core/AdjustableCropperBackground';
import 'react-advanced-cropper/dist/style.css'
import 'react-advanced-cropper/dist/themes/corners.css';

// The polyfill for Safari browser. The dynamic require is needed to work with SSR
if (typeof window !== 'undefined') {
	require('context-filter-polyfill');
}

// 4:5 Aspect Ratio for Fassion Photo
const CROP_WIDTH = 400;
const CROP_HEIGHT = 500;

export const ImageEditor = ({
	setImage
}: {
	setImage: Function
}) => {
	const cropperRef = useRef<FixedCropperRef>(null);
	const previewRef = useRef<CropperPreviewRef>(null);

	const [src, setSrc] = useState('');

	// 'brightness' | 'hue' | 'saturation' | 'contrast' | 'crop'
	const [mode, setMode] = useState('');

	const [adjustments, setAdjustments] = useState({
		brightness: 0,
		hue: 0,
		saturation: 0,
		contrast: 0,
	});

	const onChangeValue = (value: number) => {
		if (mode && mode in adjustments) {
			setAdjustments((previousValue) => ({
				...previousValue,
				[mode]: value,
			}));
		}
	};

	const onReset = () => {
		setMode('crop');
		setAdjustments({
			brightness: 0,
			hue: 0,
			saturation: 0,
			contrast: 0,
		});
	};

	const onUpload = (blob: string) => {
		onReset();
		setMode('crop');
		setSrc(blob);
	};

	const onDownload = () => {
		if (cropperRef.current) {
			// const newTab = window.open();
			// if (newTab) {
				const data = cropperRef.current.getCanvas()?.toDataURL();
				// newTab.document.body.innerHTML = `<img src="${data}"/>`;
				setImage(data);
			// }
		}
	};

	const onUpdate = (cropper: CropperRef) => {
		previewRef.current?.update(cropper);
	};

	const changed = Object.values(adjustments).some((el) => Math.floor(el * 100));

	const cropperEnabled = mode === 'crop';

	return (
		<div className={'image-editor'}>
			<div className="image-editor__cropper">
				<FixedCropper
					src={src}
					ref={cropperRef}
					stencilSize={{
						width: CROP_WIDTH,
						height: CROP_HEIGHT
					}}
					stencilProps={{
						grid: true,
						movable: cropperEnabled,
						resizable: cropperEnabled,
						lines: cropperEnabled,
						handlers: cropperEnabled,
						overlayClassName: cn(
							'image-editor__cropper-overlay',
							!cropperEnabled && 'image-editor__cropper-overlay--faded',
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
					<Slider className="image-editor__slider" 
							defaultValue={ [adjustments[mode as keyof typeof adjustments]] } 
							onValueChange={i => onChangeValue(i[0])}
					/>
				)}
				{/* <CropperPreview
					className={'image-editor__preview'}
					ref={previewRef}
					backgroundComponent={AdjustablePreviewBackground}
					backgroundProps={adjustments}
				/> */}
				<Button
					className={cn('image-editor__reset-button', !changed && 'image-editor__reset-button--hidden')}
					onClick={onReset}
				>
					<Undo />
				</Button>
			</div>
			<Navigation mode={mode} onChange={setMode} onUpload={onUpload} onDownload={onDownload} /> 
		</div>
	);
};