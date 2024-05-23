FROM gradle:jdk17-alpine AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon --exclude-task test

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]