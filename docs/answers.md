# Practicum 1 Bonus Questions

## 1.

A self-contained JAR packages both the application and its embedded server, which simplifies deployment, scaling, and independent execution. Each service is allowed to manage its own set of dependencies and server configurations without being tied to a shared application server. This approach enhances consistency in deployments and supports cloud-native services that can be deployed independently.

## 2.

Tomcat has a maximum of 50 worker threads configured, meaning that if all are in use, any additional requests will need to wait in a queue for an available thread. To manage higher loads, you can either increase the number of worker threads or deploy additional application instances. In cloud-native environments, horizontal scaling by adding more instances is typically the preferred method, as it allows for better distribution of requests across several servers.

## 3.

Spring Boot can handle multiple HTTP requests at the same time, which means that different threads might access or modify the repository concurrently. Using a standard HashMap in this scenario isn’t safe as it doesn’t support thread safety, leading to potential data inconsistencies or issues. Instead, a ConcurrentHashMap is a better choice since it allows for thread-safe access concurrently, making it suitable for the shared in-memory repository.

## 4.

An HTTP 400 Bad Request error usually signals that something is wrong with the request itself, such as missing or incorrectly formatted required fields. Even if a duplicate ISBN request contains properly formatted JSON data, the server will reject it since the ISBN already exists. HTTP 422 Unprocessable Content helps clarify this situation by indicating a semantic validation failure rather than just a malformed request.

## 5.

The @WebMvcTest annotation is beneficial because it loads only the web layer, resulting in faster and more isolated controller tests. However, it does have a limitation: since services and repositories are mocked or left out, it cannot validate whether the entire application functions cohesively. Alternatively, @SpringBootTest with RANDOM_PORT initializes the entire application and allows for real HTTP interactions across its layers.

## 6.

The commit stage should be designed to be quick so developers can get their feedback promptly after code is pushed, allowing them to address any issues before moving forward. If a test slows down the process and exceeds the time limits, it needs to be optimized or potentially moved to a different testing phase, such as a later integration-test pipeline. The focus during the commit stage should remain on fast, essential checks that can catch common issues without delaying feedback too much.