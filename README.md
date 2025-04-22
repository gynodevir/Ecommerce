# 🛒 Ecommerce API – Spring Boot Backend

[GitHub Repository](https://github.com/gynodevir/Ecommerce)

---

## 📌 Overview

This project is a backend REST API for an **E-commerce platform**, built with **Spring Boot**, **Spring Data JPA**, and **MySQL**. It provides CRUD functionality for managing product **categories**, along with exception handling, pagination, and a layered architecture.

---

## ✅ Features Implemented

### 📁 Category Module

- Create Category ✅
- Get All Categories with Pagination ✅
- Update Category ✅
- Delete Category ✅
- DTO <-> Entity Mapping using ModelMapper ✅

---

## 🔧 Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- ModelMapper
- Postman (for API Testing)
- Git & GitHub

---

## 📦 API Endpoints

| Method | Endpoint                                          | Description                          |
| ------ | ------------------------------------------------- | ------------------------------------ |
| GET    | `/api/public/categories?pageNumber=0&pageSize=10` | Fetch all categories with pagination |
| POST   | `/api/admin/categories`                           | Add a new category                   |
| PUT    | `/api/admin/categories/{categoryId}`              | Update category by ID                |
| DELETE | `/api/admin/categories/{categoryId}`              | Delete category by ID                |

---

## 📂 Project Structure

```
com.ecommerce.project
├── controller
│   └── CategoryController.java
├── service
│   ├── CategoryService.java
│   └── CategoryServiceImp.java
├── model
│   └── Category.java
├── payload
│   ├── CategoryDTO.java
│   └── CategoryResponse.java
├── exceptions
│   ├── ResourceNotFoundException.java
│   └── ApiException.java
├── repository
│   └── CategoryRepository.java
└── EcommerceApplication.java
```

---

## 📦 DTO and Response

### 📄 CategoryDTO.java

```java
public class CategoryDTO {
    private Long categoryid;
    private String categoryName;
    // Add productCount in the future
}
```

### 📄 CategoryResponse.java

```java
public class CategoryResponse {
    private List<CategoryDTO> content;
    // Add pagination details later (pageNumber, pageSize, etc.)
}
```

---

## ❗ Exception Handling

### 📄 ResourceNotFoundException

Thrown when a requested category is not found.

```java
throw new ResourceNotFoundException("Category", "categoryid", categoryid);
```

### 📄 ApiException

Used for custom business errors like duplicate category names.

```java
throw new ApiException("Category with name already exists!!");
```

---

## 🧠 Architecture Diagram

```
[Client/Postman]
       |
       v
[Controller Layer] ---> CategoryController
       |
       v
[Service Layer] ---> CategoryService -> CategoryServiceImp
       |
       v
[Repository Layer] ---> CategoryRepository
       |
       v
[Database] ---> MySQL
```

---

## 🧪 Sample Requests

### Create Category (POST)

```json
POST /api/admin/categories
{
  "categoryName": "Electronics"
}
```

### Get All Categories (GET)

```
GET /api/public/categories?pageNumber=0&pageSize=10
```

### Update Category (PUT)

```json
PUT /api/admin/categories/1
{
  "categoryName": "Updated Electronics"
}
```

### Delete Category (DELETE)

```
DELETE /api/admin/categories/1
```

---

## 🚧 To-Do List

-

---

## 🙌 Contributing

Feel free to fork and contribute. PRs are welcome!

---

## 📩 Contact

Made with ❤️ by Prasanth

For any queries, email: [[your-email@example.com](mailto:your-email@example.com)]
