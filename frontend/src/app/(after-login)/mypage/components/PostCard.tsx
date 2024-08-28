import Image from 'next/image'
import Link from 'next/link'
import { API_PUBLIC_URL } from '@/app/_common/constants'
import './postcard.scss' // 스타일 파일 임포트

interface PostCardProps {
    imageId: number
    postContent: string
    likeCount: number
    postId: number
    hashtags: string[] // 추가: 해시태그 배열
}

export default function PostCard({
    imageId,
    postContent,
    likeCount,
    postId,
    hashtags, // 추가: 해시태그 배열
}: PostCardProps) {
    return (
        <div className='postcard-container'>
            <div className='postcard-header'>
                <Link href={`/posts/${postId}`}>
                    <Image
                        src={`${API_PUBLIC_URL}/image/${imageId}`}
                        alt={postContent}
                        width={500} // 예시로 width 설정
                        height={500} // 예시로 height 설정
                        className='postcard-image'
                        unoptimized={true}
                    />
                </Link>
                <div className='postcard-content'>
                    <p className='postcard-text'>{postContent}</p>
                    <div className='postcard-hashtags'>
                        {hashtags.map((tag, index) => (
                            <span key={index} className='postcard-hashtag'>
                                #{tag}
                            </span>
                        ))}
                    </div>
                </div>
            </div>
            <div className='postcard-footer'>
                <p className='postcard-likes'>좋아요 {likeCount}개</p>
            </div>
        </div>
    )
}
