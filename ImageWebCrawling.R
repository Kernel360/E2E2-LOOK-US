## Install packages
setRepositories(ind = 1:7)


library("RSelenium")
library("rJava")
library("XML")
library("rvest")
library(httr)  
library(XML) 

## 사용자 설정 url 및 키워드드
work_dir <- ("")
setwd(work_dir)
keyword <- ""


## 크롬 드라이버 연결
remDr<-remoteDriver(remoteServerAddr="localhost", port=4445L, browserName="chrome")
remDr$open()

## 전체 화면
remDr$setWindowSize(width = 1920, height = 1080)

## URL 연결
url <- "https://www.google.com/imghp?hl=ko&ogbl"
remDr$navigate(url)

# 검색창 요소 찾기
search_box <- remDr$findElement(using = "css", "textarea.gLFyf")

# 검색어 입력, 디렉토리도 되기 때문에 특수문자 절대 주의(검증 로직 없음)
search_box$sendKeysToElement(list(keyword))

# 이미지 다운로드 디렉토리 설정 
download_dir <- file.path(work_dir, "/images/", keyword)

# 이미지 다운로드 폴더가 없다면 생성
if (!dir.exists(download_dir)) {
  dir.create(download_dir, recursive = TRUE)
}


# Enter 키를 눌러 검색 실행
search_box$sendKeysToElement(list(key = "enter"))

# 검색 결과 로딩 대기 (예: 페이지가 로드될 때까지 대기)
Sys.sleep(2)  # 로딩 시간을 고려해 적절히 조정

elements <- remDr$findElements(using = "xpath", value = "//div[@jsname='dTDiAc']")

num_clicks <- min(30, length(elements))


# 각 이미지 요소를 클릭하고, 상세보기 창의 '뒤로가기' 버튼을 클릭
for (i in seq_len(num_clicks)) {
  # 이미지 요소 클릭
  element <- elements[[i]]
  element$clickElement()
  
  # 상세보기 창 로딩 대기
  Sys.sleep(5)
  
  # 이미지 요소 찾기
  img_element <- remDr$findElement(using = "css", "div[jsname='figiqf'] img")
  
  # 이미지 URL 추출
  img_url <- img_element$getElementAttribute("src")[[1]]
  print(img_url)
  
  # 파일 이름 설정
  img_name <- paste0("image_", i, ".jpg")
  img_path <- file.path(download_dir, img_name)
  
  # 이미지 다운로드
  tryCatch({
    download.file(img_url, img_path, mode = "wb")
    message(paste("Downloaded:", img_name))
  }, error = function(e) {
    message("Failed to download:", img_url)
  })
  
  # '뒤로가기' 버튼 찾기
  back_button <- remDr$findElement(using = "css", "div[jsname='vbgB1c']")
  
  # '뒤로가기' 버튼 클릭
  back_button$clickElement()
  
  # 다시 로딩 대기
  Sys.sleep(2)
  
  # 이미지 요소를 다시 찾아서 업데이트
  elements <- remDr$findElements(using = "xpath", value = "//div[@jsname='dTDiAc']")
}



