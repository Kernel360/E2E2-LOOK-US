import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { format } from "date-fns"
import { z } from "zod"

import { Button } from "@/components/ui/button"
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
import { useNavigate } from "react-router-dom"
import { requestSignUp } from "@/api/requestSignUp"
import { Calendar } from "@/components/ui/calendar"
import { useState } from "react"
import ImageUpload from "../image-upload/ImageUpload"
import { Popover, PopoverContent, PopoverTrigger } from "@radix-ui/react-popover"
import { CalendarIcon } from "lucide-react"
import { cn } from "@/lib/utils"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@radix-ui/react-select"


export const formSchemaSignUp = z.object({
    nickname: z.string().min(2, {
        message: "이름은 2글자 이상이어야 합니다."
    }),
    birth: z.date({
        required_error: "A date of birth is required.",
    }),
    gender: z.enum(["MAN", "WOMAN", "NONE"]),
    instaId: z.string(),
});

export default function SignUpForm() {

    const WIDTH = 250;
    const HEIGHT = 250;

    const navigate = useNavigate();

    // const defaultImageUrl = `${API_URL}/image`;
    const defaultImageUrl = "";
    const [imageFile, setImageFile] = useState<string>(defaultImageUrl);

    const form = useForm<z.infer<typeof formSchemaSignUp>>({
        resolver: zodResolver(formSchemaSignUp),
        defaultValues: {
            nickname: "",
        },
    })

    function onSubmit(values: z.infer<typeof formSchemaSignUp>) {

        console.log(values);

        // TODO: send to server
        (async () => {
            const response = await requestSignUp(values, imageFile); // 로그인 Request 전송
            const responseBody = await response.json();
            if (response.ok) {
                navigate("/");
                alert('회원 가입이 완료되었습니다. 재로그인 해주시기 바랍니다');
            }
        })(/* IIFE */);
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">

                <ImageUpload thumbnail={imageFile} setThumbnail={setImageFile} width={WIDTH} height={HEIGHT} />

                <FormField
                    control={form.control}
                    name="nickname"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Nickname</FormLabel>
                            <FormControl>
                                <Input placeholder="닉네임을 입력해주세요" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="birth"
                    render={({ field }) => (
                        <FormItem className="flex flex-col">
                            <FormLabel>Date of birth</FormLabel>
                            <Popover>
                                <PopoverTrigger asChild>
                                    <FormControl>
                                        <Button
                                            variant={"outline"}
                                            className={cn(
                                                "w-[240px] pl-3 text-left font-normal",
                                                !field.value && "text-muted-foreground"
                                            )}
                                        >
                                            {field.value ? (
                                                format(field.value, "PPP")
                                            ) : (
                                                <span>Pick a date</span>
                                            )}
                                            <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                                        </Button>
                                    </FormControl>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto p-0" align="start">
                                    <Calendar
                                        mode="single"
                                        selected={field.value}
                                        onSelect={field.onChange}
                                        disabled={(date) =>
                                            date > new Date() || date < new Date("1900-01-01")
                                        }
                                        initialFocus
                                    />
                                </PopoverContent>
                            </Popover>
                            <FormDescription>
                                Your date of birth is used to calculate your age.
                            </FormDescription>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="gender"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>성별</FormLabel>
                            <Select onValueChange={field.onChange}>
                                <SelectTrigger className="w-[180px]">
                                    <SelectValue placeholder="Gender" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="WOMAN">여성</SelectItem>
                                    <SelectItem value="MAN">남성</SelectItem>
                                    <SelectItem value="NONE">없음</SelectItem>
                                </SelectContent>
                            </Select>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <Button type="submit">회원가입</Button>
            </form>
        </Form>
    )
}