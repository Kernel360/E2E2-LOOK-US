import React, { ChangeEvent, FC, useRef } from 'react';
import cn from 'classnames';
import { Blend, Check, Crop, SwatchBook } from 'lucide-react';
import { Contrast } from 'lucide-react';
import { Sun } from 'lucide-react';
import { Upload } from 'lucide-react';
import { Download } from 'lucide-react';
import './Navigation.scss';
import { Button } from '@/components/ui/button';

interface Props {
	className?: string;
	mode?: string;
	onChange?: (mode: string) => void;
	onDownload?: () => void;
	onUpload?: (blob: string) => void;
}

export const Navigation: FC<Props> = ({ 
	className, onChange, onUpload, onDownload, mode 
}) => {
	const setMode = (mode: string) => () => {
		onChange?.(mode);
	};

	const inputRef = useRef<HTMLInputElement>(null);

	const onUploadButtonClick = () => {
		inputRef.current?.click();
	};

	const onLoadImage = (event: ChangeEvent<HTMLInputElement>) => {
		// Reference to the DOM input element
		const { files } = event.target;

		// Ensure that you have a file before attempting to read it
		if (files && files[0]) {
			if (onUpload) {
				onUpload(URL.createObjectURL(files[0]));
			}
		}
		// Clear the event target value to give the possibility to upload the same image:
		event.target.value = '';
	};

	return (
		<div className={cn('image-editor-navigation', className)}>
			<Button className={'image-editor-navigation__button'} onClick={onUploadButtonClick}>
				<Upload />
				<input
					ref={inputRef}
					type="file"
					accept="image/*"
					onChange={onLoadImage}
					className="image-editor-navigation__upload-input"
				/>
			</Button>

			{ mode &&
				<div className="image-editor-navigation__buttons">
					<Button
						className={'image-editor-navigation__button'}
						variant={mode !== 'crop' ? 'outline' : 'default'}
						onClick={setMode('crop')}
					>
						<Crop />
					</Button>
					<Button
						className={'image-editor-navigation__button'}
						variant={mode !== 'saturation' ? 'outline' : 'default'}
						onClick={setMode('saturation')}
					>
						<Blend />
					</Button>
					<Button
						className={'image-editor-navigation__button'}
						variant={mode !== 'brightness' ? 'outline' : 'default'}
						onClick={setMode('brightness')}
					>
						<Sun />
					</Button>
					<Button
						className={'image-editor-navigation__button'}
						variant={mode !== 'contrast' ? 'outline' : 'default'}
						onClick={setMode('contrast')}
					>
						<Contrast />
					</Button>
					<Button className={'image-editor-navigation__button'} 
							variant={mode !== 'hue' ? 'outline' : 'default'}
							onClick={setMode('hue')}
					>
						<SwatchBook />
					</Button>
				</div>
			}
			{ mode &&
				<Button className={'image-editor-navigation__button'} 
						onClick={onDownload}
				>

					<Check className=' mr-2' /> Done
				</Button>
			}
		</div>
	);
};