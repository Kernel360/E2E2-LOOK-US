'use client'

import { BsFillEraserFill, BsSearch } from 'react-icons/bs'
import { useCallback, useState } from 'react'

interface HeaderSearchProps {
    onSearch: (search: string) => void
}

const HeaderSearch = ({ onSearch }: HeaderSearchProps) => {
    const [search, setSearch] = useState<string>('')
    const [isFocused, setIsFocused] = useState<boolean>(false)

    const handleSearchValue = useCallback(
        (e: React.ChangeEvent<HTMLInputElement>) => {
            setSearch(e.target.value)
        },
        [],
    )

    const handleFocus = () => setIsFocused(true)
    const handleBlur = () => setIsFocused(false)

    const triggerSearch = () => {
        onSearch(search)
    }

    return (
        <div
            style={{
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
                width: '100%',
                padding: '8px 16px',
                backgroundColor: isFocused ? '#d0d0d0' : '#f0f0f0',
                boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
                transition: 'background-color 0.3s ease',
                height: '45px',
                marginBottom: '20px',
            }}
        >
            {!isFocused && <BsSearch size={20} onClick={triggerSearch} />}
            <input
                style={{
                    flex: 1,
                    border: 'none',
                    outline: 'none',
                    padding: '8px',
                    fontSize: '16px',
                    borderRadius: '20px',
                    backgroundColor: 'transparent',
                    transition: 'background-color 0.3s ease',
                }}
                type='text'
                placeholder='검색'
                autoComplete='off'
                value={search}
                onChange={handleSearchValue}
                onFocus={handleFocus}
                onBlur={handleBlur}
                onKeyDown={e => e.key === 'Enter' && triggerSearch()} // 엔터 키로도 검색 가능
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
                    onClick={triggerSearch}
                    onMouseEnter={e =>
                        (e.currentTarget.style.backgroundColor = '#cccccc')
                    }
                    onMouseLeave={e =>
                        (e.currentTarget.style.backgroundColor = 'transparent')
                    }
                >
                    <BsFillEraserFill size={20} />
                </div>
            )}
        </div>
    )
}

export default HeaderSearch
