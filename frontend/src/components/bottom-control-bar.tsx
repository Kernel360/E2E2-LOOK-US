;('')
/**
 * v0 by Vercel.
 * @see https://v0.dev/t/SZO01XbAEuN
 * Documentation: https://v0.dev/docs#integrating-generated-code-into-your-nextjs-app
 */
import { Button } from '@/components/ui/button'
import Link from 'next/link'

export default function BottomControlBar() {
    return (
        <div className='fixed bottom-0 border-t border-t-gray-200 left-0 z-50 w-full bg-background shadow-t'>
            <div className='flex justify-center'>
                <nav className='flex flex-1 max-w-6xl h-20 items-center justify-between px-4'>
                    <Link href={'/posts'}>
                        <Button variant='ghost'>
                            <HomeIcon className='h-6 w-6' />
                            <span className='sr-only'>Home</span>
                        </Button>
                    </Link>
                    <Button variant='ghost'>
                        <ExpandIcon className='h-6 w-6' />
                        <span className='sr-only'>Explore</span>
                    </Button>

                    {/* ---------------------------------------------------------------- */}
                    <Link href={'/posts/new'}>
                        <Button className='bg-black p-2 text-primary-foreground shadow-md'>
                            <PlusIcon className='h-8 w-8' />
                            <span className='sr-only'>Add</span>
                        </Button>
                    </Link>
                    {/* ---------------------------------------------------------------- */}
                    <Link href={'/mypage'}>
                        <Button variant='ghost'>
                            <UsersIcon className='h-6 w-6' />
                            <span className='sr-only'>Profile</span>
                        </Button>
                    </Link>
                    <Link href={'/settings'}>
                        <Button variant='ghost'>
                            <SettingsIcon className='h-6 w-6' />
                            <span className='sr-only'>Settings</span>
                        </Button>
                    </Link>
                </nav>
            </div>
        </div>
    )
}

function ExpandIcon(props: any) {
    return (
        <svg
            {...props}
            xmlns='http://www.w3.org/2000/svg'
            width='24'
            height='24'
            viewBox='0 0 24 24'
            fill='none'
            stroke='currentColor'
            strokeWidth='2'
            strokeLinecap='round'
            strokeLinejoin='round'
        >
            <path d='m21 21-6-6m6 6v-4.8m0 4.8h-4.8' />
            <path d='M3 16.2V21m0 0h4.8M3 21l6-6' />
            <path d='M21 7.8V3m0 0h-4.8M21 3l-6 6' />
            <path d='M3 7.8V3m0 0h4.8M3 3l6 6' />
        </svg>
    )
}

function HomeIcon(props: any) {
    return (
        <svg
            {...props}
            xmlns='http://www.w3.org/2000/svg'
            width='24'
            height='24'
            viewBox='0 0 24 24'
            fill='none'
            stroke='currentColor'
            strokeWidth='2'
            strokeLinecap='round'
            strokeLinejoin='round'
        >
            <path d='m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z' />
            <polyline points='9 22 9 12 15 12 15 22' />
        </svg>
    )
}

function PlusIcon(props: any) {
    return (
        <svg
            {...props}
            xmlns='http://www.w3.org/2000/svg'
            width='24'
            height='24'
            viewBox='0 0 24 24'
            fill='none'
            stroke='currentColor'
            strokeWidth='2'
            strokeLinecap='round'
            strokeLinejoin='round'
        >
            <path d='M5 12h14' />
            <path d='M12 5v14' />
        </svg>
    )
}

function SettingsIcon(props: any) {
    return (
        <svg
            {...props}
            xmlns='http://www.w3.org/2000/svg'
            width='24'
            height='24'
            viewBox='0 0 24 24'
            fill='none'
            stroke='currentColor'
            strokeWidth='2'
            strokeLinecap='round'
            strokeLinejoin='round'
        >
            <path d='M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z' />
            <circle cx='12' cy='12' r='3' />
        </svg>
    )
}

function UsersIcon(props: any) {
    return (
        <svg
            {...props}
            xmlns='http://www.w3.org/2000/svg'
            width='24'
            height='24'
            viewBox='0 0 24 24'
            fill='none'
            stroke='currentColor'
            strokeWidth='2'
            strokeLinecap='round'
            strokeLinejoin='round'
        >
            <path d='M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2' />
            <circle cx='9' cy='7' r='4' />
            <path d='M22 21v-2a4 4 0 0 0-3-3.87' />
            <path d='M16 3.13a4 4 0 0 1 0 7.75' />
        </svg>
    )
}
