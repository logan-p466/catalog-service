# Catalog Service REST API Demonstration

This document demonstrates the REST API endpoints implemented for the Polar Bookshop Catalog Service, including successful requests, validation errors, and exception handling.

**Base URL:** `http://localhost:9001`

## 1. Create a Book (201 Created)

**Request:**

```powershell
http POST :9001/books isbn=1234567892 title="Northern Lights" author="Lyra Silverstar" price:=9.90
```

**Response:**

```http
HTTP/1.1 201
Content-Type: application/json

{
    "author": "Lyra Silverstar",
    "isbn": "1234567892",
    "price": 9.9,
    "title": "Northern Lights"
}
```

## 2. List All Books (200 OK)

**Request:**

```powershell
http GET :9001/books
```

**Response:**

```http
HTTP/1.1 200
Content-Type: application/json

[
    {
        "author": "Lyra Silverstar",
        "isbn": "1234567892",
        "price": 9.9,
        "title": "Northern Lights"
    }
]
```

## 3. Retrieve an Existing Book (200 OK)

**Request:**

```powershell
http GET :9001/books/1234567892
```

**Response:**

```http
HTTP/1.1 200
Content-Type: application/json

{
    "author": "Lyra Silverstar",
    "isbn": "1234567892",
    "price": 9.9,
    "title": "Northern Lights"
}
```

## 4. Create a Book With a Duplicate ISBN (422 Unprocessable Entity)

**Request:**

```powershell
http POST :9001/books isbn=1234567892 title="Northern Lights" author="Lyra Silverstar" price:=9.90
```

**Response:**

```http
HTTP/1.1 422
Content-Type: application/json

A book with ISBN 1234567892 already exists.
```

## 5. Submit Invalid Book Data (400 Bad Request)

**Request:**

```powershell
http POST :9001/books isbn=123ABC456Z title="" author="Jon Snow" price:=9.90
```

**Response:**

```http
HTTP/1.1 400
Content-Type: application/json

{
    "isbn": "The ISBN format must be valid.",
    "title": "The title must be defined."
}
```

## 6. Retrieve a Nonexistent Book (404 Not Found)

**Request:**

```powershell
http GET :9001/books/9999999999
```

**Response:**

```http
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8

The book with ISBN 9999999999 was not found.
```

## 7. Update an Existing Book (200 OK)

**Request:**

```powershell
http PUT :9001/books/1234567892 isbn=1234567892 title="Updated Title" author="Lyra Silverstar" price:=12.50
```

**Response:**

```http
HTTP/1.1 200
Content-Type: application/json

{
    "author": "Lyra Silverstar",
    "isbn": "1234567892",
    "price": 12.5,
    "title": "Updated Title"
}
```

## 8. Delete a Book (204 No Content)

**Request:**

```powershell
http DELETE :9001/books/1234567892
```

**Response:**

```http
HTTP/1.1 204
```

The request succeeds with no response body, as specified by HTTP 204.