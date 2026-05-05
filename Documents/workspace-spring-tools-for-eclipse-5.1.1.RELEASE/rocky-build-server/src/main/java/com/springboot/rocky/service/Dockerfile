FROM openjdk:21-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar

FROM openjdk:21-slim
WORKDIR /app

RUN apt-get update && apt-get install -y \
	curl \
	&& curl -fsSL https://get.docker.com | sh \
	&& rm -rf /var/lib/apt/lists/*

COPY --from=build /app/build/libs/*.jar app.jar

RUN mkdir -p /opt/rocky-build/work /opt/rocky-build/share

ENTRYPOINT ["java", "-jar", "app.jar"]