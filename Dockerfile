# Stage 1: Build stage with custom Gradle on JDK 21
FROM openjdk:21-slim AS gradle-builder

# 설치에 필요한 패키지 설치
RUN apt-get update && apt-get install -y wget unzip && rm -rf /var/lib/apt/lists/*

# Gradle 버전 설정 (예: 8.1.1)
ENV GRADLE_VERSION=8.1.1

# Gradle 다운로드 및 설치
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -P /tmp \
    && unzip /tmp/gradle-${GRADLE_VERSION}-bin.zip -d /opt/gradle \
    && rm /tmp/gradle-${GRADLE_VERSION}-bin.zip

ENV GRADLE_HOME=/opt/gradle/gradle-${GRADLE_VERSION}
ENV PATH=${GRADLE_HOME}/bin:${PATH}

WORKDIR /app
COPY . .

# 빌드 실행
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon -x test --stacktrace

# Stage 2: Runtime stage using JDK 21
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=gradle-builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
