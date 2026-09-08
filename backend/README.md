# Backend — Spring Boot REST API

## Stack
- Spring Boot 3.2 (Web, Security, Data MongoDB, Validation)
- MongoDB
- JWT (jjwt)

## Run locally

1. Make sure MongoDB is running (default: `mongodb://localhost:27017/blogapp`).
2. Optionally set env vars:
   ```bash
   export JWT_SECRET="a-long-random-production-secret"
   export CORS_ORIGINS="http://localhost:5173"
   ```
3. Start the app:
   ```bash
   mvn spring-boot:run
   ```

The API starts on `http://localhost:8080`.

## Package layout

```
com.blogapp
├── config/       # Security, CORS, Mongo auditing
├── controller/    # REST endpoints
├── dto/           # Request/response payloads
├── exception/     # Custom exceptions + global handler
├── model/         # MongoDB documents (User, Post, Comment)
├── repository/    # Spring Data Mongo repositories
├── security/       # JWT filter, JWT util, UserDetails
└── service/        # Business logic
```

## Authentication flow
1. `POST /api/auth/register` → creates a `User` with role `USER`, returns a JWT.
2. `POST /api/auth/login` → validates credentials, returns a JWT.
3. Client sends `Authorization: Bearer <token>` on subsequent requests.
4. `JwtAuthFilter` validates the token and loads the authenticated user into the Spring Security context.

## RBAC
- `USER` — can create posts, edit/delete their own posts and comments.
- `ADMIN` — can edit/delete any post or comment, and manage users via `/api/admin/**`.
- Enforced at two layers: URL-level rules in `SecurityConfig`, and method-level `@PreAuthorize` on controllers/ownership checks in the service layer.
