# Inventory Intelligence Platform

A Real-Time Smart Inventory & Order Intelligence Platform built using Spring Boot and PostgreSQL.

## Overview

Inventory Intelligence Platform is a backend application designed to manage products, inventory levels, and stock movements.

The system provides REST APIs for product management and inventory operations while maintaining stock movement history in PostgreSQL.

## Tech Stack

- Java 17
- Spring Boot 4.1.2
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Lombok
- REST APIs

## Features

- Product creation and management
- Product retrieval by ID
- Retrieve all products
- Product SKU validation
- Inventory stock management
- Stock movement tracking
- Purchase stock recording
- Stock increase and decrease operations
- Reorder-level tracking
- Global exception handling
- PostgreSQL database integration

## Project Structure

```text
src/main/java/com/inventory/inventoryplatform

├── controller
├── dto
├── entity
├── exception
├── repository
└── service
```

## API Endpoints

### Products

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/products` | Create a product |
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |

### Inventory

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/inventory/{productId}/purchase` | Add purchased stock |
| POST | `/api/inventory/{productId}/sell` | Reduce stock after sale |
| GET | `/api/inventory/{productId}/movements` | Get stock movement history |

## Example Product Request

```json
{
  "sku": "LG-FRIDGE-001",
  "name": "LG 260L Refrigerator",
  "description": "260L double-door refrigerator",
  "costPrice": 28000,
  "sellingPrice": 32999,
  "currentStock": 25,
  "reorderLevel": 5
}
```

## Database

The application uses PostgreSQL.

Database:

```text
inventory_platform
```

Database credentials are provided through environment variables and are not stored directly in the repository.

## Running the Application

Clone the repository:

```bash
git clone https://github.com/omdahiwale27/inventory-intelligence-platform.git
```

Navigate to the project:

```bash
cd inventory-intelligence-platform
```

Set the database password as an environment variable.

Windows PowerShell:

```powershell
$env:DB_PASSWORD="your_postgresql_password"
```

Run the application:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## Future Enhancements

- JWT authentication and authorization
- Low-stock alerts
- Sales order management
- Supplier management
- Inventory dashboard
- Advanced inventory analytics
- Kafka-based real-time events
- Docker containerization
- Cloud deployment

## Author

**Om Dahiwale**

GitHub: https://github.com/omdahiwale27