# 1. 빌드 스테이지 (JDK 21 사용)
FROM openjdk:21-slim AS build
WORKDIR /app
COPY . .
# Gradle 또는 Maven 빌드 실행 (여기서는 Gradle 가정)
RUN ./gradlew clean bootJar

# 2. 실행 스테이지
FROM openjdk:21-slim
WORKDIR /app

# 필수 패키지 및 Docker CLI 설치 (DockerService의 'docker' 명령어 실행을 위함)
RUN apt-get update && apt-get install -y \
    curl \
    && curl -fsSL https://get.docker.com | sh \
    && rm -rf /var/lib/apt/lists/*

# 빌드 스테이지에서 생성된 jar 복사
COPY --from=build /app/build/libs/*.jar app.jar

# AppConfig.java에서 정의한 호스트 경로와 통신할 디렉토리 생성
RUN mkdir -p /opt/rocky-build/work /opt/rocky-build/share

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]