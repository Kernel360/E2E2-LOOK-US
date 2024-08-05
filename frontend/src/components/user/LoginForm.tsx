import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"

import { Button } from "@/components/ui/button"
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { requestLogIn } from "@/api/DUMMY/requestLogIn"
import { useNavigate } from "react-router-dom"

export const formSchema = z.object({
    nickname: z.string(),
    email: z.string(),
    password: z.string(),
});

export default function LoginForm() {

    const navigate = useNavigate();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            email: "",
            password: "",
            nickname: "",
        },
    })

    function onSubmit(values: z.infer<typeof formSchema>) {

        console.log(values);

        // TODO: send to server
        (async () => {
            const response = await requestLogIn(values); // 로그인 Request 전송
            const responseBody = await response.json();
            alert(responseBody);

            if (response.ok) {
                navigate("/");
            }
        })(/* IIFE */);
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                <FormField
                    control={form.control}
                    name="email"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Email</FormLabel>
                            <FormControl>
                                <Input type="email" placeholder="이메일 주소를 입력해주세요" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="password"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Password</FormLabel>
                            <FormControl>
                                <Input type="password" placeholder="비밀번호를 입력해주세요" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <Button type="submit">로그인</Button>
            </form>
        </Form>
    )
}