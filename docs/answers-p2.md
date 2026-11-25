# Practicum 2 Bonus Questions

## 1.

Naming profiles after specific features, instead of environments, helps keep the application flexible. When profiles are tied to environments, any time a new environment is added, code changes might be necessary to support it. However, if we name a profile after a feature, like testdata, that feature can be activated in any environment without modifying the code.

## 2.

The JVM system property takes priority over both the environment variable and the value in application.yml, so it is the one that gets used. Environment variables are still the recommended approach because they work consistently across operating systems and container platforms, and they let you change configuration without rebuilding the application or modifying the packaged application itself.
## 3.

The Catalog Service achieves a stateless design since its book data is stored in PostgreSQL, rather than being held in the memory of individual application instances. This means all instances can access the same database and gain the same catalog information. Consequently, a load balancer can direct requests among multiple instances without tying requests for a specific book to a single instance. Additionally, an instance can be restarted or swapped out without any loss to the catalog.

## 4.

Testing against PostgreSQL ensures that our test environment closely mirrors the production setting since both utilize the same database engine and SQL dialect. Relying on H2 could lead to discrepancies in how data types, SQL syntax, constraints, and transactions behave. Testcontainers allow for the creation of PostgreSQL databases for automated tests that can be easily cleaned up after testing, eliminating the need for a dedicated permanent test database.

## 5.

Flyway keeps track of each migration that’s been applied, along with its checksum, in the flyway_schema_history table. If someone modifies an existing migration, it alters its checksum, leading to a validation failure with Flyway since it no longer aligns with what was initially applied. To avoid issues, it’s best to leave existing migrations untouched and to introduce schema changes through new versioned migrations.

## 6.

JdbcAggregateTemplate allows us to insert test data without relying on the repository methods we’re actually testing. If we were to use bookRepository.save for this setup, any problems that arise with that method could confuse unrelated repository tests or obscure the source of the issue. By utilizing a more straightforward persistence tool for setting up our tests, we can better isolate the behavior of the repository methods we want to evaluate.