FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/service-registry-0.0.1-SNAPSHOT.jar service-registry.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "service-registry.jar"]
