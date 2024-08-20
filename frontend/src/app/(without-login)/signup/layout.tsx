import { Metadata } from 'next'

export const metadata: Metadata = {
    title: 'Sign Up',
    description: 'User registration page.',
}

interface SignUpLayoutProps {
    children: React.ReactNode
}

export default function SignUpLayout({ children }: SignUpLayoutProps) {
    return (
        <div className=''>
            <section className=''>{children}</section>
        </div>
    )
}
