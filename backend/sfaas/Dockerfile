# pom.xml에 명시된 Java 17 버전에 맞는 OpenJDK 이미지를 사용합니다.
FROM openjdk:17-jdk-slim

# pom.xml의 artifactId와 version을 기반으로 생성될 JAR 파일 경로를 변수로 지정합니다.
ARG JAR_FILE=target/sfaas-0.0.1-SNAPSHOT.jar

# 빌드된 JAR 파일을 컨테이너 내부의 app.jar로 복사합니다.
COPY ${JAR_FILE} app.jar

# 컨테이너가 시작될 때 애플리케이션을 실행합니다.
ENTRYPOINT ["java","-jar","/app.jar"]