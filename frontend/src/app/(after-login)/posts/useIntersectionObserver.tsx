import { useEffect, useRef } from 'react'

export const useIntersectionObserver = (
    callback: () => void,
    hasMore: boolean,
) => {
    const observer = useRef<IntersectionObserver | null>(null)
    const lastElementRef = useRef<HTMLDivElement | null>(null)

    useEffect(() => {
        if (observer.current) observer.current.disconnect()

        observer.current = new IntersectionObserver(entries => {
            if (entries[0].isIntersecting && hasMore) {
                callback()
            }
        })

        if (lastElementRef.current) {
            observer.current.observe(lastElementRef.current)
        }

        return () => {
            if (observer.current) observer.current.disconnect()
        }
    }, [callback, hasMore])

    return lastElementRef
}
