FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar /app/app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]