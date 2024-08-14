import { Metadata } from 'next'
import Image from 'next/image'
import { PlusCircledIcon } from '@radix-ui/react-icons'

import { Button } from '@/components/ui/button'
import { ScrollArea, ScrollBar } from '@/components/ui/scroll-area'
import { Separator } from '@/components/ui/separator'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'

import { AlbumArtwork } from './components/album-artwork'
import { listenNowAlbums, madeForYouAlbums } from './data/albums'
import MyPageInfoComponent from './components/mypageinfo'

export const metadata: Metadata = {
    title: 'Music App',
    description: 'Example music app using the components.',
}

export default function MusicPage() {
    return (
        <div className='container'>
            <MyPageInfoComponent />{' '}
            {/* 사용자 이름, 프로필 이미지, 팔로워 팔로잉 확인 */}
            <div className='hidden md:block'>
                <div className='border-t'>
                    <div className='bg-background'>
                        <div className='grid lg:grid-cols-5'>
                            <div className='col-span-3 lg:col-span-4 lg:border-l'>
                                <div className='h-full px-4 py-6 lg:px-8'>
                                    <Tabs
                                        defaultValue='music'
                                        className='h-full space-y-6'
                                    >
                                        <TabsContent
                                            value='music'
                                            className='border-none p-0 outline-none'
                                        >
                                            <div className='flex items-center justify-between'>
                                                <div className='space-y-1'>
                                                    <h2 className='text-2xl font-semibold tracking-tight'>
                                                        내 업로드
                                                    </h2>
                                                    <p className='text-sm text-muted-foreground'>
                                                        내가 업로드한 글
                                                        확인하기
                                                    </p>
                                                </div>
                                            </div>
                                            <Separator className='my-4' />
                                            <div className='relative'>
                                                <ScrollArea>
                                                    <div className='flex space-x-4 pb-4'>
                                                        {listenNowAlbums.map(
                                                            album => (
                                                                <AlbumArtwork
                                                                    key={
                                                                        album.name
                                                                    }
                                                                    album={
                                                                        album
                                                                    }
                                                                    className='w-[250px]'
                                                                    aspectRatio='portrait'
                                                                    width={250}
                                                                    height={330}
                                                                />
                                                            ),
                                                        )}
                                                    </div>
                                                    <ScrollBar orientation='horizontal' />
                                                </ScrollArea>
                                            </div>
                                            <div className='mt-6 space-y-1'>
                                                <h2 className='text-2xl font-semibold tracking-tight'>
                                                    내 좋아요
                                                </h2>
                                                <p className='text-sm text-muted-foreground'>
                                                    내가 좋아요한 스타일
                                                    확인하기
                                                </p>
                                            </div>
                                            <Separator className='my-4' />
                                            <div className='relative'>
                                                <ScrollArea>
                                                    <div className='flex space-x-4 pb-4'>
                                                        {listenNowAlbums.map(
                                                            album => (
                                                                <AlbumArtwork
                                                                    key={
                                                                        album.name
                                                                    }
                                                                    album={
                                                                        album
                                                                    }
                                                                    className='w-[250px]'
                                                                    aspectRatio='portrait'
                                                                    width={250}
                                                                    height={330}
                                                                />
                                                            ),
                                                        )}
                                                    </div>
                                                    <ScrollBar orientation='horizontal' />
                                                </ScrollArea>
                                            </div>
                                            <div className='mt-6 space-y-1'>
                                                <h2 className='text-2xl font-semibold tracking-tight'>
                                                    내 스크랩
                                                </h2>
                                                <p className='text-sm text-muted-foreground'>
                                                    내가 스크랩한 스타일
                                                    확인하기
                                                </p>
                                            </div>
                                            <Separator className='my-4' />
                                            <div className='relative'>
                                                <ScrollArea>
                                                    <div className='flex space-x-4 pb-4'>
                                                        {listenNowAlbums.map(
                                                            album => (
                                                                <AlbumArtwork
                                                                    key={
                                                                        album.name
                                                                    }
                                                                    album={
                                                                        album
                                                                    }
                                                                    className='w-[250px]'
                                                                    aspectRatio='portrait'
                                                                    width={250}
                                                                    height={330}
                                                                />
                                                            ),
                                                        )}
                                                    </div>
                                                    <ScrollBar orientation='horizontal' />
                                                </ScrollArea>
                                            </div>
                                            <div className='mt-6 space-y-1'>
                                                <h2 className='text-2xl font-semibold tracking-tight'>
                                                    해시태그
                                                </h2>
                                                <p className='text-sm text-muted-foreground'>
                                                    내가 자주 찾는 해시태그
                                                </p>
                                            </div>
                                            <Separator className='my-4' />
                                            <div className='relative'>
                                                <ScrollArea>
                                                    <div className='flex space-x-4 pb-4'>
                                                        {madeForYouAlbums.map(
                                                            album => (
                                                                <AlbumArtwork
                                                                    key={
                                                                        album.name
                                                                    }
                                                                    album={
                                                                        album
                                                                    }
                                                                    className='w-[150px]'
                                                                    aspectRatio='square'
                                                                    width={150}
                                                                    height={150}
                                                                />
                                                            ),
                                                        )}
                                                    </div>
                                                    <ScrollBar orientation='horizontal' />
                                                </ScrollArea>
                                            </div>
                                        </TabsContent>
                                    </Tabs>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
