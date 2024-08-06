import ReactPaginate from 'react-paginate';

import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination"
import { useState } from 'react';

export interface PaginatedItemsProps {
  totalPageCount: number,
  currentPageNumber: number,
  setCurrentPageNumber: Function
}

export function PaginatedItems({
  totalPageCount, 
  currentPageNumber,
  setCurrentPageNumber
}: PaginatedItemsProps) {

  const PAGEABLE_RANGE_DEFAULT = 2;

  const [startPageNumber, setStartPageNumber] = useState<number>(1);

  const renderPageNumberButtons = () => {
    return new Array(PAGEABLE_RANGE_DEFAULT)
      .fill(0)
      .map((_, idx) => (
        <PaginationItem>
          <PaginationLink
            href="#"
            isActive={currentPageNumber === (startPageNumber + idx)}
            onClick={() => {
              setCurrentPageNumber(startPageNumber + idx);
            }}
          >
            { startPageNumber + idx }
          </PaginationLink>
        </PaginationItem>
      ))
  }

  return (
    <Pagination>
      <PaginationContent>
        {/* Previous */}
        <PaginationItem>
          <PaginationPrevious
            href="#"
            onClick={() => {
              if (startPageNumber <= 1) {
                return;
              }
              setStartPageNumber((prev) => (prev - PAGEABLE_RANGE_DEFAULT))
              setCurrentPageNumber(startPageNumber);
            }}
          />
        </PaginationItem>

        {renderPageNumberButtons()}

        {/* ... */}
        <PaginationItem>
          <PaginationEllipsis />
        </PaginationItem>

        {/* Next */}
        <PaginationItem>
          <PaginationNext
            href="#"
            onClick={() => {
              if (totalPageCount <= (startPageNumber + PAGEABLE_RANGE_DEFAULT)) {
                return;
              }
              setStartPageNumber((prev) => (prev + PAGEABLE_RANGE_DEFAULT))
              setCurrentPageNumber(startPageNumber);
            }}
          />
        </PaginationItem>

      </PaginationContent>
    </Pagination>
  );
}
