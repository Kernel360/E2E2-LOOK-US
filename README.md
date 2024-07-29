# E2E2-LOOK-US
kernel360 E2E 프로젝트 / 패션 커뮤니티 서비스, LOOK:US 리포지토리입니다.


### 팀 개발 환경 가이드

#####  (1) 포매터 설정
- JDK : JAVA 17, JDK
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
