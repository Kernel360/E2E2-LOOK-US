// middleware.ts
import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

export function middleware(req: NextRequest) {
    const token = req.cookies.get('login')?.value

    // 쿠키에 'login' 값이 없으면 회원가입 페이지로 리다이렉트
    if (!token) {
        return NextResponse.redirect(new URL('/signup', req.url))
    }

    return NextResponse.next()
}

// 이 미들웨어를 적용할 경로를 설정합니다.
export const config = {
    matcher: '/(after-login)/mypage/:path*', // 예시로 MyPage 관련 경로에 적용
}
