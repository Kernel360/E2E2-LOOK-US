import { QuestionPreviewData } from "@/components/question-list/QuestionPreviewCard";
import { API_URL } from "../API_URL";

export interface GET_paginationRequestFormat {
  pagination: {
    currentPage: number              // 현재 페이지
    pageSize: number,         // 한 페이지에 몇개가 들어가는지 
  },
  sort: {            // 기본 전략은 최신순 + 내림차순 입니다.
    target:
    "createdAt" |
    "views" |
    "likes" |
    "hates"
  }
}

export interface GET_paginationResponseFormat {
  pagination: {
    currentPage: number,            // 몇번째 페이지인지
    totalPage: number,       // 전체 페이지 수
  },
  // 질문 내용 미리보기 UI를 위한 데이터입니다.
  questionPreviews: QuestionPreviewData[];
}

export async function requestQuestionListPaginated(
  page: number,
  pageSize: number,
  sort?: "views" | "likes" | "hates",
  searchCategory?: string,
  searchQuery?: string | null,
) {

  let requestUrl = `${API_URL}/questions`;

  if (searchQuery) {
    switch (searchCategory) {
      case ("title"):
        requestUrl = `${API_URL}/questions?title=${searchQuery}`;
        break;
      case ("content"):
        requestUrl = `${API_URL}/questions?content=${searchQuery}`;
        break;
      case ("nickname"):
        requestUrl = `${API_URL}/questions?email=${searchQuery}`;
        break;
      case ("email"):
        requestUrl = `${API_URL}/questions?email=${searchQuery}`;
        break;
      default:
        alert("검색 카테고리를 설정해주세요!");
        break;
    }
  }

  const requestPayload: GET_paginationRequestFormat = {
    pagination: {
      currentPage: page,
      pageSize: pageSize,
    },
    sort: {
      target: sort ?? "createdAt" // default to view
    },
  };

  const questionListReponse = await fetch(requestUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(requestPayload),
  });


  // on error
  if (!questionListReponse.ok) {
    const errorData = await questionListReponse.json();
    alert(errorData);
    return ;
  }

  const body: GET_paginationResponseFormat = await questionListReponse.json();
  return body;
}