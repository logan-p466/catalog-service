# Polar Bookshop Catalog Service

A REST API for managing a book catalog, developed with Java 21 and Spring Boot 3.5 for CSCI-P466 at Indiana University.

## Features

- Create, retrieve, update, and delete books using REST endpoints.
- Validate book information using Jakarta Bean Validation.
- Store books in an in-memory repository.
- Test application behavior with JUnit, MockMvc, and Spring Boot Test.
- Build and test automatically using GitHub Actions.

## Running the Application

Build the project:

```powershell
.\gradlew.bat build
```

Start the application:

```powershell
.\gradlew.bat bootRun
```

The API runs at `http://localhost:9001`.

## CI Status

[![Commit Stage](https://github.com/logan-p466/catalog-service/actions/workflows/commit-stage.yml/badge.svg)](https://github.com/logan-p466/catalog-service/actions/workflows/commit-stage.yml)

## API Documentation

See [API Demonstration](docs/api-demo.md) for example HTTP requests and responses.