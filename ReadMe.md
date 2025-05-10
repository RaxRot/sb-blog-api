# 📝 Spring Boot Blog REST API with JWT Authentication

A secure, RESTful blog API built using Spring Boot, JWT, and role-based access control. Users can register, authenticate, and interact with posts and comments based on their roles.

---

## 📌 Features

- User registration and login with JWT
- Role-based access control (ADMIN / USER)
- CRUD operations for Posts (ADMIN only)
- CRUD operations for Comments (authenticated users)
- Pagination and sorting for posts
- Input validation and global exception handling
- Swagger/OpenAPI documentation
- Custom error responses (403, 401, 400, 404)

---

## 🛠️ Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Security
- JWT (jjwt)
- Spring Data JPA
- Hibernate
- PostgreSQL / H2 (depending on config)
- Maven
- Lombok
- ModelMapper
- Swagger (springdoc-openapi)

---

## 📂 Project Structure

```
com.example.blog
├── config
│   └── SecurityConfiguration.java
├── controller
│   ├── AuthController.java
│   ├── PostController.java
│   └── CommentController.java
├── dto
│   ├── PostDTO.java
│   ├── CommentDTO.java
│   ├── LoginDTO.java
│   ├── RegisterDTO.java
│   ├── JWTAuthResponse.java
│   └── PostResponse.java
├── entity
│   ├── Post.java
│   ├── Comment.java
│   ├── User.java
│   └── Role.java
├── exception
│   ├── GlobalExceptionHandler.java
│   └── BlogAPIException.java
├── repository
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   ├── PostRepository.java
│   └── CommentRepository.java
├── security
│   ├── JWTAuthenticationFilter.java
│   ├── JWTAuthenticationEntryPoint.java
│   └── JwtTokenProvider.java
├── service
│   ├── AuthServiceImpl.java
│   ├── PostServiceImpl.java
│   └── CommentServiceImpl.java
└── BlogApplication.java
```

---

## 🔐 Security Overview

- `/api/auth/**` – open to everyone (register/login)
- `GET /api/**` – public (view posts, comments)
- `POST/PUT/DELETE /api/posts/**` – ADMIN only
- `POST/PUT/DELETE /api/posts/{id}/comments/**` – authenticated users

JWT is sent in `Authorization: Bearer <token>` header.

---

## 🧪 Sample Requests

### 🔑 Login
```http
POST /api/auth/login
{
  "usernameOrEmail": "user@example.com",
  "password": "password123"
}
```

### 📝 Create Post (ADMIN only)
```http
POST /api/posts
Authorization: Bearer <token>
{
  "title": "First Post",
  "description": "Intro",
  "content": "Hello World!"
}
```

---

## 🚀 Running the App

1. Clone the repo  
2. Configure your DB and `application.properties`  
3. Run the app via `main()` or `mvn spring-boot:run`  
4. Test at `http://localhost:8080/swagger-ui/index.html`  

---

## 🧠 Final Notes

- Passwords are stored encoded with `BCrypt`
- Stateless JWT auth, no sessions
- Follows SOLID principles
- Good starting point for full-stack blog apps

---

## 💻 Author

Created during course-based learning and improved step-by-step by following best practices.