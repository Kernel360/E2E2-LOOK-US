import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
  } from "@/components/ui/card"

export interface props {
    question: QuestionPreviewData;
}

export interface QuestionPreviewData {
    questionId: string;
    title: string;
    nickname: string;
    createdAt: string;
    likes: number;
    hates: number;
    views: number;
}

export default function QuestionPreviewCard( {question}: props ) {
    return (
        <Card className="w-full max-w-xl">
            <CardHeader>
                <CardTitle>{question.title}</CardTitle>
                <CardDescription>
                    {question.nickname} / created at {question.createdAt}
                </CardDescription>
            </CardHeader>
            <CardContent>
                <p>질문 내용 프리뷰...</p>
            </CardContent>
        </Card>
    );
}