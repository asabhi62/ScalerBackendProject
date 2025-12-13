FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/project-0.0.1-SNAPSHOT.jar project-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "project-service.jar"]
