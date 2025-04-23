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
- DTO ↔ Entity Mapping using ModelMapper ✅
- Default Pagination in `AppConfig` > `AppConstants` ✅
- Integrated Swagger API documentation ✅
- Configured CORS for cross-origin requests ✅


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
### AppConfig Enhancements
Description: Configured application-wide settings.

Changes:

- Set up default pagination parameters.

- Configured CORS settings to allow cross-origin requests.

- Integrated Swagger for API documentation.

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
{
    "content": [
        {
            "categoryid": 1,
            "categoryName": "defddewffewwfdvfefeeeffefew3fgwcegfgfewf"
        }
    ],
    "pageNumber": 0,
    "pageSize": 50,
    "totalElements": 1,
    "totalPages": 1,
    "lastPage": true
}
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


---

## 🙌 Contributing

Feel free to fork and contribute. PRs are welcome!

---

## 📩 Contact

Made with ❤️ by Prasanth

For any queries, email: [[jprasanth7989@gmail.com]()]
