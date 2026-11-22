# Bus Ticket Booking System

A microservices-based bus ticket booking system built with **Java and Spring Boot**. The project focuses on distributed system architecture, reliable seat reservation, asynchronous communication, and secure payment workflows.

## Overview

The system allows users to search for bus trips, select seats, make reservations, process payments, and receive booking confirmations.

It is designed with independent services, database separation, and event-driven communication to improve scalability and maintainability.

## Architecture

The system follows a microservices architecture with an API Gateway as the single entry point.

### Services

| Service | Responsibility |
|---|---|
| API Gateway | Request routing, JWT authentication, rate limiting |
| Discovery Server | Service registration and discovery using Eureka |
| Auth Service | User authentication and authorization |
| Route Service | Bus routes, stations, and stops |
| Trip Service | Trips, vehicles, schedules, and pricing |
| Seat Service | Seat reservation and availability management |
| Booking Service | Booking management and Saga coordination |
| Payment Service | Payment processing |
| Notification Service | Booking confirmations and notifications |

## Technology Stack

- **Backend:** Java 17+, Spring Boot 3, Spring Cloud
- **Security:** Spring Security, JWT
- **Database:** PostgreSQL
- **Caching & Locking:** Redis
- **Messaging:** Apache Kafka / RabbitMQ
- **Resilience:** Resilience4j
- **Migration:** Flyway
- **Containerization:** Docker, Docker Compose
- **Testing:** JUnit 5, Testcontainers
- **Monitoring:** Prometheus, Grafana, Zipkin
- **CI/CD:** GitHub Actions

## Key Features

- Microservices architecture with independent databases.
- Centralized API Gateway and JWT-based authentication.
- Distributed service discovery using Eureka.
- Atomic seat reservation using Redis to prevent double booking.
- Database constraints as an additional safeguard against duplicate seat reservations.
- Event-driven communication between services.
- Saga pattern for distributed booking and payment transactions.
- Idempotent event processing and failure compensation.
- Fault tolerance with timeout, retry, and circuit breaker mechanisms.
- Containerized deployment using Docker Compose.

## Booking Workflow

1. Users search for available trips and select seats.
2. The Booking Service requests the Seat Service to temporarily reserve selected seats.
3. A pending booking is created and a payment event is published.
4. The Payment Service processes the transaction.
5. On success, seats are confirmed and the booking status becomes `CONFIRMED`.
6. On failure or expiration, seats are released and the booking is cancelled.

## Getting Started

### Prerequisites

- Java 17 or later
- Maven
- Docker and Docker Compose
- Git

### Clone the Repository

```bash
git clone <repository-url>
cd <project-directory>
```

### Run the Infrastructure

```bash
docker compose up -d
```

### Run the Services

Start the Spring Boot services individually, beginning with the Discovery Server and API Gateway, followed by the business services.

> Detailed setup instructions and environment configuration will be added as the project evolves.

## Project Status

<text>🚧 **In Development**</text>

This project is being developed incrementally, with a focus on distributed transactions, concurrency control, service communication, and system reliability.

## License

This project is intended for educational and portfolio purposes.
