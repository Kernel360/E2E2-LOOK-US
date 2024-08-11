import { Suspense } from 'react'
import { getAllPostPreviews, postPreviewContent } from '../../_api/fetchStyle'
import StylePreview from '@/components/post-preview'
import './gallery.scss'
import { Separator } from '@radix-ui/react-select'

/**
 * @link https://nextjs.org/docs/app/api-reference/file-conventions/route-segment-config#revalidate
 *
 *       - 기본 값은 false로, next 서버에서 미리 만들어둔 html을 계속 보내주기 때문에
 *         api에서 새로운 데이터를 줘도, html이 갱신되지 않는다.
 *
 *       - 따라서 일단 revalidate을 0초로 잡고 요청마다 계속 html를 새롭게 생성하도록 했다.
 *         그런데, 굳이 다른 사람이 올린 글을 실시간으로 볼 필요는 없다.
 *         내 패션이 업로드 된 것도 꼭 즉시 보여줄 필요도 없다...
 *         이건 프론트와의 고민이 필요한 내용인 것 같다.
 */
export const revalidate = 0

/**
 * https://github.com/gitdagray/nextjs-image-gallery-ninja/blob/lesson-9/src/app/components/Gallery.tsx
 */
export default async function Gallery() {
    const styles: postPreviewContent[] = (await getAllPostPreviews()).content

    return (
        <section className='gallery'>
            {styles?.map((item, index) => (
                <StylePreview
                    className='gallery-item'
                    content={item}
                    key={index}
                />
            ))}
        </section>
    )
}
