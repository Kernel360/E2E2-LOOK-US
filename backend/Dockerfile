# 1단계: Gradle과 JDK 17을 사용하여 애플리케이션을 빌드
FROM gradle:8.4-jdk17 AS build

# 애플리케이션의 작업 디렉토리 설정
WORKDIR /backend

# Gradle 빌드 파일과 소스 코드 복사
COPY build.gradle ./
COPY src ./src

# 애플리케이션 빌드
RUN gradle build --no-daemon

# 2단계: Corretto 17을 사용하여 애플리케이션을 실행
FROM amazoncorretto:17-alpine

# 빌드된 JAR 파일을 컨테이너에 복사
COPY --from=build /backend/build/libs/backend-0.0.1-SNAPSHOT.jar /backend/backend-0.0.1-SNAPSHOT.jar

# 애플리케이션이 실행될 포트 번호 설정
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "/backend/backend-0.0.1-SNAPSHOT.jar"]
