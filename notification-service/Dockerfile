FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar /app/app.jar
EXPOSE 8083
CMD ["java", "-jar", "app.jar"]