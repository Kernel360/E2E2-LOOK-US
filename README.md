# ✨ LOOK:US ✨

> 나만의 데일리룩을 공유해요
> MZ 남녀의 요즘 "패션"은 **LOOK:US**에서!

![룩어스 대표이미지](./LOOKUS_대표이미지.jpg)

## 📖 Description

데일리룩 (OOTD:Outfit Of The Day)을 공유하며 서로의 패션 스타일을 공유하고, 컨셉에 맞는 룩을 골라 살펴볼 수 있는 패션 커뮤니티 서비스입니다.

## :baby_chick: Demo

## ⭐ Main Feature

### 메인 화면

- 이미지, 해시태그 기반으로 업로드된 데일리룩, OOTD 확인 가능
- 해시태그 기반 필터링, 게시물 검색 가능

### 게시물 작성

- 이미지 업로드 (필수)
- 게시글 작성
- 카테고리 기반 해시태그 선택 후 포스팅

### 팔로우 기능

- 해당 사용자의 스타일이 맘에 드는 경우 -> 팔로우
- 팔로워가 1000명 이상인 유저 -> 인플루언서 권한 부여 (작성한 게시글이 인플루언서 피드에도 노출됨)

### 회원가입 및 로그인

- JWT 이용

### 기타 기능

- 댓글 및 좋아요
- 마이페이지

## 💻 Getting Started

(↑해당 프로젝트 설치 및 실행 방법)

### Installation

```
npm install
```

### Develop Mode

```
npm run dev
```

### Production

```
npm run build
```

## 🔧 Stack

- FRONT
  - Next.js
- BACK
  - Spring Boot
  - Spring Security, OAuth, JWT, Validation, Lombok,
  - 고려해볼것: Redis, WebFlux
- DataBase
  - MySQL / NOSQL?
- Infra
  - AWS or Naver
- Deploy
  - AWS EC2

## :open_file_folder: Project Structure

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

- 협의 후 추가 예정입니다.

##### (3) 도커 설정

- 도커는 나중에 협의 후 추가 예정입니다.

## 👨‍👩‍👧‍👦 Developer

- **김민주** ([Minju-Kimm](https://github.com/Minju-Kimm))
- **김민규** ([kimminkyeu](https://github.com/kimminkyeu))
- **김영래** ([tigris24](https://github.com/tigris24))
- **임건우** ([limbaba1120](https://github.com/limbaba1120))
