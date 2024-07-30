FROM amazoncorretto-17
ARG JAR_FILE=build/libs/e2e2-look-us-0.0.1-SNAPSHOT-plain.jar
ADD ${JAR_FILE} docker-e2e2lookus.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/dockere2e2lookus.jar"]