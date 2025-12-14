# Scaler Backend Project

This is a comprehensive backend application built using Spring Boot, demonstrating a microservices architecture with Service Discovery, Shopping Cart functionality, and Product management.

## 🚀 Project Overview

The project is structured into three main components:

1.  **Service Registry (Eureka Server)**: Acts as a service discovery server where other microservices register themselves.
2.  **Product Service (Main Backend)**: The core application handling products, shopping cart, authentication, and payments. It integrates with MySQL, Redis, and Kafka.
3.  **Discovery Client**: A sample client application that registers with the Service Registry.

## 🛠️ Tech Stack

*   **Language**: Java 17+
*   **Framework**: Spring Boot
*   **Build Tool**: Maven
*   **Database**: MySQL (Persistent storage)
*   **Caching**: Redis (Shopping Cart)
*   **Messaging**: Apache Kafka
*   **Containerization**: Docker & Docker Compose

## 📂 Project Structure

```
ScalerBackendProject/
├── ScalerBackendProject/    # Main Product Service Application
├── ServiceRegistry/         # Eureka Server Application
├── DiscoveryClient/         # Sample Discovery Client
├── docker-compose.yml       # Docker orchestration
└── ...
```

## 📋 Prerequisites

*   Java 17 or higher
*   Maven 3.6+
*   Docker Desktop (for running dependencies and services easily)

## 🐳 Running with Docker (Recommended)

The easiest way to run the entire ecosystem is using Docker Compose.

1.  **Build the project artifacts:**
    You may need to build the JAR files first if the Dockerfiles expect them, or if the Docker build process handles it (multi-stage). Assuming standard Maven build:

    ```bash
    # Build Service Registry
    cd ServiceRegistry
    mvn clean package -DskipTests
    cd ..

    # Build Discovery Client
    cd DiscoveryClient
    mvn clean package -DskipTests
    cd ..

    # Build Main Project
    cd ScalerBackendProject
    mvn clean package -DskipTests
    cd ..
    ```

2.  **Start the services:**

    ```bash
    docker-compose up --build
    ```

    This command will start:
    *   **MySQL** (Database)
    *   **Redis** (Cache)
    *   **Kafka** & **Zookeeper** (Messaging)
    *   **Service Registry** on port `8761`
    *   **Product Service** on port `8080`
    *   **Discovery Client** on port `8081`

## 🏃‍♂️ Manual Setup

If you prefer to run services locally without Docker Compose for the Java apps:

1.  **Start Infrastructure**: You still need MySQL, Redis, and Kafka running locally.
2.  **Service Registry**:
    ```bash
    cd ServiceRegistry
    mvn spring-boot:run
    ```
    Access at: `http://localhost:8761`

3.  **Product Service**:
    Update `application.properties` to point to your local MySQL/Redis/Kafka if needed.
    ```bash
    cd ScalerBackendProject
    mvn spring-boot:run
    ```
    Access at: `http://localhost:8080`

4.  **Discovery Client**:
    ```bash
    cd DiscoveryClient
    mvn spring-boot:run
    ```

## 🔌 API Endpoints

### Product Service (Port 8080)
*   **Health Check**: `GET /hello`
*   **Products**: `GET /products`, `POST /products`, etc.
*   **Cart**: Endpoints for managing shopping cart (backed by Redis).

### Service Registry (Port 8761)
*   **Dashboard**: Visit `http://localhost:8761` to see registered services.

## 🧪 Running Tests

To run unit and integration tests for the main application:

```bash
cd ScalerBackendProject
mvn test
```
