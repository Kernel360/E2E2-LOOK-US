"use client"

import { zodResolver } from "@hookform/resolvers/zod"
import { CalendarIcon, CaretSortIcon, CheckIcon } from "@radix-ui/react-icons"
import { ChevronDownIcon } from "@radix-ui/react-icons"
import { format } from "date-fns"
import { useFieldArray, useForm } from "react-hook-form"
import { z } from "zod"

import { Button, buttonVariants } from "./ui/button"
import { Calendar } from "./ui/calendar"

import { cn } from "@/lib/utils"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"

import { Textarea } from "./ui/textarea"
import { useEffect, useState } from "react"
import { ImageEditor } from "./image-editor/image-editor"
import { Separator } from "@radix-ui/react-select"
import { Delete, Plus, X } from "lucide-react"
import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar"
import { createPost } from "@/app/_api/post"

const ACCEPTED_IMAGE_MIME_TYPES = [
  "image/jpeg",
  "image/jpg",
  "image/png",
  "image/webp",
];
const ACCEPTED_IMAGE_TYPES = ["jpeg", "jpg", "png", "webp"];
const MAX_FILE_SIZE = 1024 * 1024 * 5; // 5ML

const contentFormSchema = z.object({
    image: z
      .any(),
        // .refine((files) => {
        //   return files?.[0]?.size <= MAX_FILE_SIZE;
        // }, `Max image size is 5MB.`)
        // .refine(
        //   (files) => ACCEPTED_IMAGE_MIME_TYPES.includes(files?.[0]?.type),
        //   "Only .jpg, .jpeg, .png and .webp formats are supported."
        // ),
    content: z
        .string()
        .min( 0, {
        message: "Content must be at least 2 characters.",
        })
        .max(80, {
        message: "Content must not be longer than 30 characters.",
        }),
    hashtags: z
        .array(
            z.object({
            value: z.string(),
            })
        )
        .optional(),
})

export type PostFormValues = z.infer<typeof contentFormSchema>

// This can come from your database or API.
const defaultValues: Partial<PostFormValues> = {
  image: undefined,
  hashtags: [
    { value: "" }
  ]
};

export function PostContentForm() {

  const [image, setImage] = useState<string | null>(null);

  const form = useForm<PostFormValues>({
    resolver: zodResolver(contentFormSchema),
    defaultValues,
  })

  const { fields, append, remove } = useFieldArray({
    name: "hashtags",
    control: form.control,
  })

  function onSubmit(data: PostFormValues) {
    createPost(data);
  }

  useEffect(() => {
    if (image) {
      form.setValue("image", image);
    }
  }, [image]);

  return (
    <div>

      { !image && 
      <div className=" shadow-md shadow-gray-100">
        <ImageEditor 
          setImage={setImage} 
        />
      </div>
      }

      {/* image submitted preview */}
      { image &&
          <Avatar className=" flex justify-center relative">
              <AvatarImage
                className=" w-3/4"
                src={image} 
              />
              {/* reset image */}
              <Button 
                variant="outline"
                onClick={() => setImage('')}
                className=" absolute right-32 top-5 p-2 "
              >
                <X/>
              </Button>
          </Avatar>
      } 

      <Separator className="mt-8" />

      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">

          <FormField
            control={form.control}
            name="content"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Content</FormLabel>
                <FormControl>
                  <div className="grid w-full gap-2">
                      <Textarea
                          disabled={!image}
                          placeholder="Tell us a little bit about yourself"
                          className="resize-none"
                          {...field}
                      />
                  </div>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <div>
            {
            fields.map((field, index) => (

                <FormField
                  control={form.control}
                  key={field.id}
                  name={`hashtags.${index}.value`}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className={cn(index !== 0 && "sr-only")}>
                        Hashtags
                      </FormLabel>
                      <FormDescription className={cn(index !== 0 && "sr-only")}>
                        Add links to your website, blog, or social media profiles.
                      </FormDescription>

                      <div className="flex items-center space-x-3">
                        <FormControl>
                          <Input 
                            className=" max-w-56" {...field} 
                            disabled={!image}
                          />
                        </FormControl>

                        <Button 
                          disabled={!image}
                          variant="outline"
                          onClick={() => remove(index)}
                        >
                          <X/>
                        </Button>

                      </div>
                    </FormItem>
                  )}
                />

            ))}
            <Button
              type="button"
              variant="outline"
              size="sm"
              className="mt-2"
              disabled={!image}
              onClick={() => append({ value: "" })}
            >
              <Plus size={20} className=" mr-1"/> Add Hashtag
            </Button>
          </div>
        <Separator className="my-6" />
        <Button 
          size="lg" 
          className=" w-full py-8 text-xl" 
          type="submit"
          disabled={!image}
        >
          Create Post
        </Button>
        </form>
      </Form>
    </div>
  )
}