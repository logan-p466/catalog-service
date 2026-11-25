
# Practicum 2: Configuration Demonstration

## Build the JAR

The application was built once and run with four different configurations without rebuilding the JAR.

```powershell
.\gradlew.bat bootJar
```

## 1. Command-Line Argument

### Command

```powershell
java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar "--polar.greeting=Welcome to the catalog from CLI"
```

### Request

```powershell
http :9001/
```

### Response

```http
HTTP/1.1 200
Connection: keep-alive
Content-Length: 31
Content-Type: text/plain;charset=UTF-8
Date: Wed, 25 Nov 2026 04:36:43 GMT
Keep-Alive: timeout=15

Welcome to the catalog from CLI
```

## 2. JVM System Property

### Command

```powershell
java "-Dpolar.greeting=Welcome to the catalog from JVM" -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
```

### Request

```powershell
http :9001/
```

### Response

```http
HTTP/1.1 200
Connection: keep-alive
Content-Length: 31
Content-Type: text/plain;charset=UTF-8
Date: Wed, 25 Nov 2026 04:36:54 GMT
Keep-Alive: timeout=15

Welcome to the catalog from JVM
```

## 3. Environment Variable

### Command

```powershell
$env:POLAR_GREETING="Welcome to the catalog from ENV"
java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
```

### Request

```powershell
http :9001/
```

### Response

```http
HTTP/1.1 200
Connection: keep-alive
Content-Length: 31
Content-Type: text/plain;charset=UTF-8
Date: Wed, 25 Nov 2026 04:37:08 GMT
Keep-Alive: timeout=15

Welcome to the catalog from ENV
```

After stopping the application, the environment variable was removed.

```powershell
Remove-Item Env:\POLAR_GREETING
```

## 4. Command-Line Argument and JVM System Property

### Command

```powershell
java "-Dpolar.greeting=Welcome to the catalog from JVM" -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar "--polar.greeting=Welcome to the catalog from CLI"
```

### Request

```powershell
http :9001/
```

### Response

```http
HTTP/1.1 200
Connection: keep-alive
Content-Length: 31
Content-Type: text/plain;charset=UTF-8
Date: Wed, 25 Nov 2026 04:37:30 GMT
Keep-Alive: timeout=15

Welcome to the catalog from CLI
```

## Results

All four runs returned HTTP 200 and the expected greeting without rebuilding the JAR. The fourth run demonstrates that command-line arguments take precedence over JVM system properties.

The configuration sources used in this practicum have the following precedence, from highest to lowest:

1. Command-line arguments
2. JVM system properties
3. Operating system environment variables
4. Profile-specific configuration files
5. Default application.yml

This demonstrates how externalized configuration allows the same application artifact to run with different configurations.
