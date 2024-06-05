FROM gradle:8.5.0-jdk17-alpine AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon --exclude-task test --info

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar /app/app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]