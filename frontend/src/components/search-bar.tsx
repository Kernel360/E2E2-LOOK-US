'use client'

import { BsFillEraserFill, BsSearch } from 'react-icons/bs'
import { useCallback, useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'

const HeaderSearch = () => {
    const router = useRouter()
    const [search, setSearch] = useState<string>('')
    const [isFocused, setIsFocused] = useState<boolean>(false) // 포커스 상태 관리

    useEffect(() => {
        try {
            // 검색 기능 구현
        } catch (e) {
            console.error(e instanceof Error ? e.message : e)
        }
    }, [search])

    const backPage = () => {
        router.replace('/')
        setSearch('')
    }

    const handleSearchValue = useCallback(
        (e: React.ChangeEvent<HTMLInputElement>) => {
            setSearch(e.target.value)
        },
        [],
    )

    const handleFocus = () => setIsFocused(true)
    const handleBlur = () => setIsFocused(false)

    return (
        <div
            style={{
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
                width: '100%',
                padding: '8px 16px',
                borderRadius: '30px', // 더 둥글게 설정
                backgroundColor: isFocused ? '#d0d0d0' : '#f0f0f0', // 포커스에 따라 배경색 변경
                boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
                transition: 'background-color 0.3s ease', // 배경색 전환 애니메이션
                height: '45px',
                marginBottom: '20px',
            }}
        >
            {!isFocused && <BsSearch size={20} />}{' '}
            {/* 포커스가 없을 때만 검색 아이콘 표시 */}
            <input
                style={{
                    flex: 1,
                    border: 'none',
                    outline: 'none',
                    padding: '8px',
                    fontSize: '16px',
                    borderRadius: '20px', // 더 둥글게 설정
                    backgroundColor: 'transparent',
                    transition: 'background-color 0.3s ease', // 배경색 전환 애니메이션
                }}
                type='text'
                placeholder='검색'
                autoComplete='off'
                value={search}
                onChange={handleSearchValue}
                onFocus={handleFocus} // 포커스 시 상태 변경
                onBlur={handleBlur} // 포커스 해제 시 상태 변경
            />
            {search && (
                <div
                    style={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        width: '30px',
                        height: '30px',
                        borderRadius: '50%',
                        cursor: 'pointer',
                        transition: 'background-color 0.3s ease',
                    }}
                    onClick={backPage}
                    onMouseEnter={e =>
                        (e.currentTarget.style.backgroundColor = '#cccccc')
                    } // 마우스 오버 시 배경색 변경
                    onMouseLeave={e =>
                        (e.currentTarget.style.backgroundColor = 'transparent')
                    } // 마우스 떠날 시 배경색 초기화
                >
                    <BsFillEraserFill size={20} />
                </div>
            )}
        </div>
    )
}

export default HeaderSearch
