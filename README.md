# Polar Bookshop Catalog Service

A REST API for managing a book catalog, developed with Java 21 and Spring Boot 3.5 for CSCI-P466 at Indiana University.

## Features

- Create, retrieve, update, and delete books using REST endpoints.
- Validate book information using Jakarta Bean Validation.
- Search for books by author.
- Store books in PostgreSQL using Spring Data JDBC.
- Manage database schema changes using Flyway.
- Track creation dates, modification dates, and versions.
- Configure application properties externally without rebuilding.
- Enable sample data through a configurable feature flag.
- Test application behavior with JUnit, MockMvc, Spring Boot Test, and Testcontainers.
- Build and test automatically using GitHub Actions.

## Running the Application

### Prerequisites

- Java 21
- Docker Desktop or Docker Engine

### Start PostgreSQL

Create the PostgreSQL container:

```powershell
docker run -d --name polar-postgres -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=polardb_catalog -p 5432:5432 postgres:17
```

If the container already exists, start it instead:

```powershell
docker start polar-postgres
```

### Build the Project

```powershell
.\gradlew.bat clean build
```

### Run the Application

```powershell
.\gradlew.bat bootRun
```

The API runs at `http://localhost:9001`.

The `bootRun` task enables the test-data loader, which clears existing books and inserts sample data at startup.

To run the application without resetting the catalog, build and run the packaged JAR:

```powershell
.\gradlew.bat bootJar
java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
```

Flyway automatically applies pending database migrations at startup.

## CI Status

[![Commit Stage](https://github.com/logan-p466/catalog-service/actions/workflows/commit-stage.yml/badge.svg)](https://github.com/logan-p466/catalog-service/actions/workflows/commit-stage.yml)

## Documentation

- [API Demonstration](docs/api-demo.md)
- [Configuration Demonstration](docs/config-demo.md)
- [Practicum 1 Bonus Questions](docs/answers.md)
- [Practicum 2 Bonus Questions](docs/answers-p2.md)

## E2: PUT ISBN Mismatch Policy

For PUT requests, the ISBN in the URL must match the ISBN in the request body. If they differ, the API returns HTTP 400 Bad Request without modifying the catalog.

This prevents ambiguity about which book should be created or updated and ensures that the URL identifies the resource being modified.

## E4: DELETE Idempotency

Returning HTTP 204 No Content when deleting a nonexistent book is consistent with HTTP idempotency. An operation is idempotent when repeating it has the same intended effect on the server as performing it once. Whether the book exists before the DELETE request or has already been removed, the resulting state is the same: the book no longer exists. Idempotency does not require identical HTTP response codes across repeated requests.


## Practicum 2 E1: Publisher Schema Evolution

The publisher column is nullable in this release because existing books do not
have publisher information, and older instances of Catalog Service do not
provide a publisher when inserting new books. Making the column mandatory
immediately could cause older application instances to fail.

In the first release, a Flyway migration adds the nullable publisher column.
The updated application supports reading and writing publisher information
while remaining compatible with older instances.

Before the second release, all older instances are retired, new writes are
required to include a publisher, and existing rows with missing publishers
are backfilled. A subsequent Flyway migration can then add a NOT NULL
constraint to the publisher column.

This allows the database schema to evolve without requiring all application
instances to be updated at the same time.


## Practicum 2 E2: Search by Author

The catalog supports filtering books by author through GET /books?author=....

Spring Data JDBC derives the database query from the findByAuthor method in
BookRepository. If no author is provided, the endpoint returns all books.
If an author has no matching books, the endpoint returns an empty list
with HTTP 200 OK.

Author filtering is tested at both the repository and web layers.

## Practicum 2 E3: Feature Flag Without Profiles

The test-data loader uses @ConditionalOnProperty instead of
@Profile to control whether sample data is loaded.

The polar.testdata.enabled property defaults to false in
application.yml. The bootRun task enables it for local development.

The feature can also be enabled on the packaged JAR using the
POLAR_TESTDATA_ENABLED environment variable without rebuilding
the application.

See docs/config-demo.md for the demonstration.

## Practicum 2 E4: Optimistic Locking

Spring Data JDBC uses the Book version field to detect conflicting updates.

When two copies of the same book are loaded, saving the first copy increases
the version stored in the database. Attempting to save the second copy with
its outdated version causes an OptimisticLockingFailureException.

BookControllerAdvice handles this exception and returns HTTP 409 Conflict.
The repository test verifies that stale updates are rejected, and the web
test verifies that the exception is translated into the correct HTTP response.
