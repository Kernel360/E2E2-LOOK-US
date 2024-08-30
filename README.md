# ✨ LOOK:US ✨

> 나만의 데일리룩을 공유해요
> MZ 남녀의 요즘 "패션"은 **LOOK:US**에서!

![룩어스 대표이미지](./LOOKUS_대표이미지.jpg)

## 📖 Description

데일리룩 (OOTD:Outfit Of The Day)을 공유하며 서로의 패션 스타일을 공유하고, 컨셉에 맞는 룩을 골라 살펴볼 수 있는 패션 커뮤니티 서비스입니다.

## ⭐ Main Feature

### 메인 화면

- 이미지, 해시태그, 카테고리 기반으로 업로드된 데일리룩, OOTD 확인
- 옷 카테고리 기반 필터링, 게시물 검색
- 게시물 내용, 해시태그를 통한 검색
- RGB 기반 검색
  - Color Picker 를 통해 원하는 R, G, B 값을 통한 검색
  - 인기 있는 색을 클릭하면 RGB 값 자동 세팅되어 검색
- 게시물 스크랩
- 유저 팔로우

### 게시물 작성

- 이미지 업로드 (필수)
- 게시글 작성
- 해시태그 작성
- 옷 카테고리 선택

### 팔로우
- 해당 사용자의 스타일이 맘에 드는 경우 -> 팔로우

### 회원가입 및 로그인

- JWT 이용
- Google 소셜 로그인
- 추가 정보 입력

### 관리자 페이지
- 1일, 전체 통계를 그래프로 확인 가능
- 레디스에 업데이트할 색상 조건 설정 가능

### 스크랩
- 마음에 드는 게시물 스크랩
- 마이페이지에서 확인 가능

### 기타 기능

- 좋아요
- 마이페이지

## 🎨 Frontend Wireframe
![image](https://github.com/user-attachments/assets/aad0b6ab-36a5-4a84-b2fe-8dae9df73b44)

![image](https://github.com/user-attachments/assets/31b5f20d-de5d-4329-915c-1fe87ba66c74)

## 🔧 Stack

- FRONT
  - Next.js
- BACK
  - Spring Boot
  - Spring Security, OAuth, JWT
  - R
- DataBase
  - MySQL / Redis
- Infra
  - AWS or Naver
- Deploy
  - AWS EC2

## ERD
![diagram](https://github.com/user-attachments/assets/c4e677c2-36b1-4692-b8b7-8569e312a0b3)



## 📂 Directory Structure
<details>
  <summary> 📂 파일 구조 보기</summary>
  

```markdown
📦
├─ .github
│  └─ ISSUE_TEMPLATE
│     ├─ 기능-구현-완료.md
│     └─ 버그-리포트.md
├─ README.md
├─ backend
│  ├─ .gitignore
│  ├─ Dockerfile
│  ├─ build.gradle
│  ├─ data
│  │  └─ images
│  │     └─ Readme.md
│  ├─ docker-compose.yaml
│  ├─ gradle
│  │  └─ wrapper
│  │     └─ gradle-wrapper.jar
│  ├─ gradlew
│  ├─ gradlew.bat
│  └─ src
│     ├─ main
│     │  ├─ java
│     │  │  └─ org
│     │  │     └─ example
│     │  │        ├─ LookusApplication.java
│     │  │        ├─ common
│     │  │        │  └─ TimeTrackableEntity.java
│     │  │        ├─ config
│     │  │        │  ├─ JpaAuditingConfiguration.java
│     │  │        │  ├─ TokenAuthenticationFilter.java
│     │  │        │  ├─ WebOAuthSecurityConfig.java
│     │  │        │  ├─ jwt
│     │  │        │  │  ├─ JwtProperties.java
│     │  │        │  │  └─ TokenProvider.java
│     │  │        │  ├─ oauth
│     │  │        │  │  ├─ GoogleUserInfo.java
│     │  │        │  │  ├─ OAuth2AuthorizationRequestBasedOnCookieRepository.java
│     │  │        │  │  ├─ OAuth2SuccessHandler.java
│     │  │        │  │  ├─ OAuth2UserCustomService.java
│     │  │        │  │  └─ OAuth2UserInfo.java
│     │  │        │  └─ swagger
│     │  │        │     └─ SwaggerConfig.java
│     │  │        ├─ exception
│     │  │        │  ├─ common
│     │  │        │  │  ├─ ApiErrorCategory.java
│     │  │        │  │  ├─ ApiErrorResponse.java
│     │  │        │  │  ├─ ApiException.java
│     │  │        │  │  └─ ApiExceptionHandler.java
│     │  │        │  ├─ post
│     │  │        │  │  ├─ ApiPostErrorSubCategory.java
│     │  │        │  │  ├─ ApiPostException.java
│     │  │        │  │  └─ ApiPostExceptionHandler.java
│     │  │        │  ├─ storage
│     │  │        │  │  ├─ ApiStorageErrorSubCategory.java
│     │  │        │  │  ├─ ApiStorageException.java
│     │  │        │  │  └─ ApiStorageExceptionHandler.java
│     │  │        │  └─ user
│     │  │        │     ├─ ApiUserErrorSubCategory.java
│     │  │        │     ├─ ApiUserException.java
│     │  │        │     └─ ApiUserExceptionHandler.java
│     │  │        ├─ image
│     │  │        │  ├─ controller
│     │  │        │  │  └─ ImageResourceController.java
│     │  │        │  ├─ resourceLocation
│     │  │        │  │  ├─ entity
│     │  │        │  │  │  └─ ResourceLocationEntity.java
│     │  │        │  │  └─ repository
│     │  │        │  │     └─ ResourceLocationRepository.java
│     │  │        │  ├─ storage
│     │  │        │  │  ├─ CloudStorage
│     │  │        │  │  │  └─ readme.md
│     │  │        │  │  ├─ FileSystemStorage
│     │  │        │  │  │  └─ FileSystemStorage.java
│     │  │        │  │  ├─ core
│     │  │        │  │  │  ├─ StoragePacket.java
│     │  │        │  │  │  ├─ StorageSaveResultInternal.java
│     │  │        │  │  │  ├─ StorageService.java
│     │  │        │  │  │  └─ StorageType.java
│     │  │        │  │  └─ strategy
│     │  │        │  │     ├─ DirectoryNamingStrategy.java
│     │  │        │  │     ├─ FileNamingStrategy.java
│     │  │        │  │     ├─ LocalDateDirectoryNamingStrategy.java
│     │  │        │  │     └─ UuidV4FileNamingStrategy.java
│     │  │        │  └─ storageManager
│     │  │        │     ├─ StorageManager.java
│     │  │        │     ├─ common
│     │  │        │     │  ├─ StorageFindResult.java
│     │  │        │     │  └─ StorageSaveResult.java
│     │  │        │     └─ imageStorageManager
│     │  │        │        └─ ImageStorageManager.java
│     │  │        ├─ post
│     │  │        │  ├─ controller
│     │  │        │  │  ├─ PostApiController.java
│     │  │        │  │  └─ PostController.java
│     │  │        │  ├─ domain
│     │  │        │  │  ├─ dto
│     │  │        │  │  │  ├─ PaginationDto.java
│     │  │        │  │  │  └─ PostDto.java
│     │  │        │  │  ├─ entity
│     │  │        │  │  │  ├─ HashtagEntity.java
│     │  │        │  │  │  ├─ PostEntity.java
│     │  │        │  │  │  └─ UserPostLikesEntity.java
│     │  │        │  │  └─ enums
│     │  │        │  │     └─ PostStatus.java
│     │  │        │  ├─ repository
│     │  │        │  │  ├─ HashtagRepository.java
│     │  │        │  │  ├─ PostRepository.java
│     │  │        │  │  └─ custom
│     │  │        │  │     ├─ PostRepositoryCustom.java
│     │  │        │  │     ├─ PostRepositoryImpl.java
│     │  │        │  │     └─ PostSearchCondition.java
│     │  │        │  └─ service
│     │  │        │     └─ PostService.java
│     │  │        ├─ user
│     │  │        │  ├─ common
│     │  │        │  │  └─ RandomName.java
│     │  │        │  ├─ controller
│     │  │        │  │  ├─ member
│     │  │        │  │  │  ├─ UserApiController.java
│     │  │        │  │  │  └─ UserViewController.java
│     │  │        │  │  └─ token
│     │  │        │  │     └─ TokenApiController.java
│     │  │        │  ├─ domain
│     │  │        │  │  ├─ dto
│     │  │        │  │  │  ├─ UserDto.java
│     │  │        │  │  │  ├─ request
│     │  │        │  │  │  │  └─ token
│     │  │        │  │  │  │     └─ CreateAccessTokenRequest.java
│     │  │        │  │  │  └─ response
│     │  │        │  │  │     └─ token
│     │  │        │  │  │        └─ CreateAccessTokenResponse.java
│     │  │        │  │  ├─ entity
│     │  │        │  │  │  ├─ BaseEntity.java
│     │  │        │  │  │  ├─ member
│     │  │        │  │  │  │  └─ UserEntity.java
│     │  │        │  │  │  └─ token
│     │  │        │  │  │     └─ RefreshToken.java
│     │  │        │  │  └─ enums
│     │  │        │  │     ├─ Gender.java
│     │  │        │  │     ├─ Role.java
│     │  │        │  │     └─ UserStatus.java
│     │  │        │  ├─ repository
│     │  │        │  │  ├─ member
│     │  │        │  │  │  └─ UserRepository.java
│     │  │        │  │  └─ token
│     │  │        │  │     └─ RefreshTokenRepository.java
│     │  │        │  └─ service
│     │  │        │     ├─ member
│     │  │        │     │  ├─ UserDetailService.java
│     │  │        │     │  └─ UserService.java
│     │  │        │     └─ token
│     │  │        │        ├─ RefreshTokenService.java
│     │  │        │        └─ TokenService.java
│     │  │        ├─ util
│     │  │        │  └─ CookieUtil.java
│     │  │        └─ validation
│     │  │           ├─ annotation
│     │  │           │  └─ CustomEmail.java
│     │  │           └─ validator
│     │  │              └─ CustomEmailValidator.java
│     │  └─ resources
│     │     ├─ static
│     │     │  └─ favicon.ico
│     │     └─ templates
│     │        ├─ articleList.html
│     │        ├─ index.html
│     │        ├─ login.html
│     │        ├─ newArticle.html
│     │        ├─ oauthLogin.html
│     │        └─ signup.html
│     └─ test
│        └─ java
│           └─ org
│              └─ example
│                 ├─ blog
│                 │  └─ presentation
│                 │     └─ article
│                 │        └─ BlogApiControllerTest.java
│                 ├─ config
│                 │  └─ jwt
│                 │     ├─ JwtFactory.java
│                 │     └─ TokenProviderTest.java
│                 ├─ post
│                 │  ├─ controller
│                 │  │  └─ PostControllerTest.java
│                 │  └─ service
│                 │     └─ PostServiceTest.java
│                 └─ user
│                    ├─ controller
│                    │  ├─ member
│                    │  │  ├─ UserApiControllerTest.java
│                    │  │  └─ UserViewControllerTest.java
│                    │  └─ token
│                    │     └─ TokenApiControllerTest.java
│                    ├─ domain
│                    │  └─ entity
│                    │     └─ member
│                    │        └─ UserEntityTest.java
│                    └─ service
│                       └─ member
│                          ├─ UserDetailServiceTest.java
│                          └─ UserServiceTest.java
├─ data
│  └─ images
│     └─ README.md
├─ formatter
│  ├─ naver-checkstyle-rules.xml
│  ├─ naver-checkstyle-supperssions.xml
│  └─ naver-intellij-formatter.xml
└─ frontend
   ├─ .eslintrc.json
   ├─ .gitignore
   ├─ .prettierrc.json
   ├─ README.md
   ├─ components.json
   ├─ next.config.mjs
   ├─ package.json
   ├─ postcss.config.mjs
   ├─ public
   │  ├─ next.svg
   │  └─ vercel.svg
   ├─ src
   │  ├─ app
   │  │  ├─ _api
   │  │  │  ├─ login.ts
   │  │  │  ├─ post.ts
   │  │  │  ├─ postPreview.ts
   │  │  │  └─ sumit.ts
   │  │  ├─ _common
   │  │  │  └─ constants.ts
   │  │  ├─ favicon.ico
   │  │  ├─ globals.css
   │  │  ├─ layout.tsx
   │  │  ├─ page.tsx
   │  │  ├─ post
   │  │  │  └─ [post_id]
   │  │  │     └─ page.tsx
   │  │  ├─ posts
   │  │  │  ├─ create
   │  │  │  │  ├─ layout.tsx
   │  │  │  │  └─ page.tsx
   │  │  │  ├─ layout.tsx
   │  │  │  ├─ loading.tsx
   │  │  │  └─ page.tsx
   │  │  ├─ signin
   │  │  │  ├─ layout.tsx
   │  │  │  └─ page.tsx
   │  │  └─ signup
   │  │     ├─ layout.tsx
   │  │     └─ page.tsx
   │  ├─ components
   │  │  ├─ Icons.tsx
   │  │  ├─ date-picker.tsx
   │  │  ├─ image-editor
   │  │  │  ├─ core
   │  │  │  │  ├─ AdjustableCropperBackground.tsx
   │  │  │  │  ├─ AdjustableImage.scss
   │  │  │  │  ├─ AdjustableImage.tsx
   │  │  │  │  ├─ AdjustablePreviewBackground.tsx
   │  │  │  │  ├─ Navigation.scss
   │  │  │  │  └─ Navigation.tsx
   │  │  │  ├─ css
   │  │  │  │  ├─ constants.scss
   │  │  │  │  └─ mixins.scss
   │  │  │  ├─ image-editor.scss
   │  │  │  └─ image-editor.tsx
   │  │  ├─ page-header.tsx
   │  │  ├─ post-create.tsx
   │  │  ├─ post-preview.tsx
   │  │  ├─ ui
   │  │  │  ├─ avatar.tsx
   │  │  │  ├─ button.tsx
   │  │  │  ├─ calendar.tsx
   │  │  │  ├─ card.tsx
   │  │  │  ├─ command.tsx
   │  │  │  ├─ dialog.tsx
   │  │  │  ├─ form.tsx
   │  │  │  ├─ input.tsx
   │  │  │  ├─ label.tsx
   │  │  │  ├─ popover.tsx
   │  │  │  ├─ select.tsx
   │  │  │  ├─ separator.tsx
   │  │  │  ├─ slider.tsx
   │  │  │  ├─ textarea.tsx
   │  │  │  ├─ toast.tsx
   │  │  │  ├─ toaster.tsx
   │  │  │  └─ use-toast.ts
   │  │  ├─ user-account-form.tsx
   │  │  ├─ user-auth-form-simple.tsx
   │  │  └─ user-auth-form.tsx
   │  └─ lib
   │     └─ utils.ts
   ├─ tailwind.config.ts
   └─ tsconfig.json
```
©generated by [Project Tree Generator](https://woochanleee.github.io/project-tree-generator)
</details>

## 🔨 Server Architecture

(↑서버 아키텍처에 대한 내용을 그림으로 표현함으로써 인프라를 어떻게 구축했는 지 한 눈에 보여줄 수 있다.)

## ⚒ CI/CD

-

## 👨‍💻 Role & Contribution

## 🔨 팀 개발 환경 가이드

##### (0) 환경

- JDK : JAVA 17, JDK amazoncorretto:17

##### (1) 포매터 설정

- IntelliJ 설정
  - Code Formatter 설정
    - ./formatter 에 위치한 `naver-intellij-formatter.xml` 파일을 이용하여 설정
    - Settings -> CodeStyle -> Java -> Import Scheme
  - CheckStyle 설정
    - Plugin CheckStyle 설치
    - ./formatter 에 위치한 `naver-checkstyle-rules.xml` 파일을 이용하여 설정
    - suppression file 의 경우 `./.idea/naver-checkstyle-suppresssions.xml` 지정
  - Format-on-save 기능 활성화
    - Settings -> Tools -> Actions on Save -> Reformat code 체크

##### (2) Env 설정

- Google Vision Api Key
  - .env 의 GOOGLE_VISION_API_KEYS

##### (3) 도커 설정

- look-us-dev
  - look-us-mysql : MySQL Docker
  - look-us-redis : Redis Docker


## 👨‍👩‍👧‍👦 Developer

- **김민주** ([Minju-Kimm](https://github.com/Minju-Kimm))
- **김민규** ([kimminkyeu](https://github.com/kimminkyeu))
- **김영래** ([tigris24](https://github.com/tigris24))
- **임건우** ([limbaba1120](https://github.com/limbaba1120))
