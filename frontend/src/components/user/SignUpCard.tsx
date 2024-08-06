import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import SignUpForm from "./SignUpForm"
import { Separator } from "@/components/ui/separator"

export default function SignUpCard() {
    return (
        <Card className=" flex flex-col w-[350px] items-center justify-center h-screen">
            <CardHeader>
                <CardTitle>회원 가입</CardTitle>
                <CardDescription>회원 가입 UI 입니다</CardDescription>
            </CardHeader>
            <CardContent>
                <SignUpForm />
            </CardContent>
        </Card>
    )
}
