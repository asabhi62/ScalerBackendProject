FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/discovery-client-0.0.1-SNAPSHOT.jar discovery-client.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "discovery-client.jar"]
