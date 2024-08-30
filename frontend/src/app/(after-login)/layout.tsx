import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import '@/app/globals.css'
import BottomControlBar from '@/components/bottom-control-bar'
import { cn } from '@/lib/utils'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
    title: 'LOOK:US',
    description: '당신만을 위한, 패션 커뮤니티 서비스',
}

export default function RootLayout({
    children,
}: Readonly<{
    children: React.ReactNode
}>) {
    return (
        <html lang='ko'>
            <body className={cn(inter.className)} style={{ margin: 0 }}>
                <main className='app-container'>{children}</main>
            </body>
        </html>
    )
}
