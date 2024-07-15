
# Weather Alerts count API

## Overview

This project is a Java Spring Boot application that provides weather alerts count with circuit breaker.

## Prerequisties
- Java : 21
- Springboot : 3.2.7

## API Reference

#### Get Alerts count

```http
  GET /api/v1/weather/alerts-count
```

## Features

- Fetch weather alerts count from external API
- User authentication
- Parameterization with paalication.properties
- Swagger documentation

## Getting Started
- Clone the repository.
- Build the project using ./mvn build.
- Run the application using ./mvn springboot:run.

## Swagger
http://localhost:8080/swagger-ui/index.html