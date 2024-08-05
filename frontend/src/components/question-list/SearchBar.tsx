import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"

import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"


interface SearchBarProps {
  searchQuery: string | null;
  onSearchChange: (value: string) => void;
  filter: string;
  onFilterChange: (value: string) => void;
  sort: string;
  onSortChange: (value: string) => void;
}


const SearchBar: React.FC<SearchBarProps> = ({ searchQuery, onSearchChange, filter, onFilterChange, sort, onSortChange }) => {
  return (
    <div className="flex w-full max-w-xl items-center space-x-2">
      <Select onValueChange={(value) => onFilterChange(value)}>
        <SelectTrigger className="w-[200px]">
          <SelectValue placeholder="검색 조건" />
        </SelectTrigger>
        <SelectContent>
          <SelectGroup>
            <SelectLabel>Category</SelectLabel>
            <SelectItem value="title">제목</SelectItem>
            <SelectItem value="content">내용</SelectItem>
            <SelectItem value="nickname">닉네임</SelectItem>
            <SelectItem value="email">이메일</SelectItem>
          </SelectGroup>
        </SelectContent>
      </Select>

      <Input type="text" placeholder="search" onChange={(e) => onSearchChange(e.target.value)} />
      <Button type="submit">검색하기</Button>

      <Select onValueChange={(value) => onSortChange(value)}>
        <SelectTrigger className="w-[200px]">
          <SelectValue placeholder="정렬 조건" />
        </SelectTrigger>
        <SelectContent>
          <SelectGroup>
            <SelectLabel>Sort</SelectLabel>
            <SelectItem value="createAt">최신 순</SelectItem>
            <SelectItem value="views">조회수 순</SelectItem>
            <SelectItem value="likes">좋아요 순</SelectItem>
            <SelectItem value="hates">싫어요 순</SelectItem>
          </SelectGroup>
        </SelectContent>
      </Select>
    </div>
  );
};

export default SearchBar;