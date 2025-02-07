FROM gradle:8.1.1-jdk17 AS builder

WORKDIR /home/gradle/project

COPY --chown=gradle:gradle . .

RUN gradle bootJar --no-daemon -x test

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
