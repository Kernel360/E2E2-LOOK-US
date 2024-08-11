'use client'

import { zodResolver } from '@hookform/resolvers/zod'
import { CalendarIcon, CaretSortIcon, CheckIcon } from '@radix-ui/react-icons'
import { ChevronDownIcon } from '@radix-ui/react-icons'
import { format } from 'date-fns'
import { useFieldArray, useForm } from 'react-hook-form'
import { z } from 'zod'

import { Button, buttonVariants } from './ui/button'
import { Calendar } from './ui/calendar'

import { cn } from '@/lib/utils'
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'

import { Textarea } from './ui/textarea'
import { useEffect, useState } from 'react'
import { ImageEditor } from './image-editor/image-editor'
import { Separator } from '@radix-ui/react-select'
import { Delete, Plus, X } from 'lucide-react'
import { Avatar, AvatarFallback, AvatarImage } from '@radix-ui/react-avatar'
import { createPost } from '@/app/_api/post'
import { cookies } from 'next/headers'
import { RequestCookie } from 'next/dist/compiled/@edge-runtime/cookies'
import { setCookie } from 'cookies-next'
import { useRouter } from 'next/navigation'
import { revalidatePath } from 'next/cache'

// const ACCEPTED_IMAGE_MIME_TYPES = [
//     'image/jpeg',
//     'image/jpg',
//     'image/png',
//     'image/webp',
// ]
// const ACCEPTED_IMAGE_TYPES = ['jpeg', 'jpg', 'png', 'webp']
// const MAX_FILE_SIZE = 1024 * 1024 * 5 // 5ML

const contentFormSchema = z.object({
    editedImageBlob: z.any(), // TODO: check input image (before editing) file format...!
    content: z
        .string()
        .min(0, {
            message: 'Content must be at least 2 characters.',
        })
        .max(80, {
            message: 'Content must not be longer than 30 characters.',
        }),
    hashtags: z
        .array(
            z.object({
                value: z.string(),
            }),
        )
        .optional(),
})

export type PostFormValues = z.infer<typeof contentFormSchema>

// This can come from your database or API.
const defaultValues: Partial<PostFormValues> = {
    editedImageBlob: undefined,
    hashtags: [{ value: '' }],
}

export interface Image {
    blob?: Blob
}

export function PostContentForm({
    accessToken,
}: {
    accessToken?: RequestCookie
}) {
    // ---------------------------------------
    // console.log(accessToken?.value);
    setCookie('token', accessToken?.value)
    const router = useRouter()

    const [image, setImage] = useState<Image | null>(null)
    const [previewImage, setPreviewImage] = useState<string>('')

    const form = useForm<PostFormValues>({
        resolver: zodResolver(contentFormSchema),
        defaultValues,
    })

    const { fields, append, remove } = useFieldArray({
        name: 'hashtags',
        control: form.control,
    })

    function onSubmit(formData: PostFormValues) {
        ;(async () => {
            // 1. send createPost request to api-server
            await createPost(formData)

            // 2. move to '/posts'
            router.push(`/posts`)

            // 3.
            router.refresh()
        })(/** IIFE */)
    }

    useEffect(() => {
        // TODO: validation request - /api/v1/user/me --> redirect to /posts
    }, [])

    useEffect(() => {
        if (image && image.blob) {
            form.setValue('editedImageBlob', image.blob)
            setPreviewImage(URL.createObjectURL(image.blob))
        }
        // Revoke the object URL, to allow the garbage collector to destroy the uploaded before file
        return () => {
            URL.revokeObjectURL(previewImage)
        }
    }, [image])

    return (
        <div className=''>
            {!image && (
                <div className=' shadow-md shadow-gray-100'>
                    <ImageEditor setImage={setImage} />
                </div>
            )}

            {/* image submitted preview */}
            {previewImage && (
                <Avatar className=' flex justify-center relative'>
                    <AvatarImage
                        className=' w-full border-t border-gray-200 shadow-sm'
                        src={previewImage}
                    />
                    {/* reset image */}
                    <Button
                        variant='outline'
                        onClick={() => setImage({ blob: undefined })}
                        className=' absolute right-32 top-5 p-2 '
                    >
                        <X />
                    </Button>
                </Avatar>
            )}

            <Separator className='mt-8' />

            <Form {...form}>
                <form
                    onSubmit={form.handleSubmit(onSubmit)}
                    className='space-y-8'
                >
                    <FormField
                        control={form.control}
                        name='content'
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Content</FormLabel>
                                <FormControl>
                                    <div className='grid w-full gap-2'>
                                        <Textarea
                                            disabled={!image || !image.blob}
                                            placeholder='Tell us a little bit about yourself'
                                            className='resize-none'
                                            {...field}
                                        />
                                    </div>
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />

                    <div>
                        {fields.map((field, index) => (
                            <FormField
                                control={form.control}
                                key={field.id}
                                name={`hashtags.${index}.value`}
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel
                                            className={cn(
                                                index !== 0 && 'sr-only',
                                            )}
                                        >
                                            Hashtags
                                        </FormLabel>
                                        <FormDescription
                                            className={cn(
                                                index !== 0 && 'sr-only',
                                            )}
                                        >
                                            Add links to your website, blog, or
                                            social media profiles.
                                        </FormDescription>

                                        <div className='flex items-center space-x-3'>
                                            <FormControl>
                                                <Input
                                                    className=' max-w-56'
                                                    {...field}
                                                    disabled={!image}
                                                />
                                            </FormControl>

                                            <Button
                                                disabled={!image}
                                                variant='outline'
                                                onClick={() => remove(index)}
                                            >
                                                <X />
                                            </Button>
                                        </div>
                                    </FormItem>
                                )}
                            />
                        ))}
                        <Button
                            type='button'
                            variant='outline'
                            size='sm'
                            className='mt-2'
                            disabled={!image || !image.blob}
                            onClick={() => append({ value: '' })}
                        >
                            <Plus size={20} className=' mr-1' /> Add Hashtag
                        </Button>
                    </div>
                    <Separator className='my-6' />
                    <Button
                        size='lg'
                        className=' w-full py-8 text-xl'
                        type='submit'
                        disabled={!image || !image.blob}
                    >
                        Create Post
                    </Button>
                </form>
            </Form>
        </div>
    )
}
