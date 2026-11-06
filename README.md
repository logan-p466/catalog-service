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

## E2: PUT ISBN Mismatch Policy

For PUT requests, the ISBN in the URL must match the ISBN in the request body. If they differ, the API returns HTTP 400 Bad Request without modifying the catalog.

This prevents ambiguity about which book should be created or updated and ensures that the URL identifies the resource being modified.

## E4: DELETE Idempotency

Returning HTTP 204 No Content when deleting a nonexistent book is consistent with HTTP idempotency. An operation is idempotent when repeating it has the same intended effect on the server as performing it once. Whether the book exists before the DELETE request or has already been removed, the resulting state is the same: the book no longer exists. Idempotency does not require identical HTTP response codes across repeated requests.