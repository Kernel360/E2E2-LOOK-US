'use client'

import { BsFillEraserFill, BsSearch } from 'react-icons/bs'
import { useCallback, useState } from 'react'
import styles from './HeaderSearch.module.scss' // SCSS 모듈을 가져와서 사용

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
        <div className={styles.headerSearch}>
            {/* {!isFocused && (
                <BsSearch
                    size={20}
                    className={styles.searchIcon}
                    onClick={triggerSearch}
                />
            )} */}
            <input
                className={styles.input}
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
                    className={styles.clearIcon}
                    onClick={() => setSearch('')} // 지우기 기능 구현
                >
                    <BsFillEraserFill size={20} />
                </div>
            )}
        </div>
    )
}

export default HeaderSearch
