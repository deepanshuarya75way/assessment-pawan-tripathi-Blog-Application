# BlogSphere — Full-Stack Blog Application

A full-stack blogging platform built with **React.js** (frontend) and **Spring Boot** (backend), using **MongoDB** as the database. Supports the complete content creation and publishing workflow, JWT-based authentication, and role-based access control (RBAC).

## ✨ Features

- 🔐 **Authentication** — JWT-based signup/login with hashed passwords (BCrypt)
- 👥 **Role-Based Access Control** — `USER` and `ADMIN` roles with distinct permissions
- ✍️ **Content Workflow** — create, edit, delete, publish/unpublish (draft) blog posts
- 💬 **Comments** — authenticated users can comment on posts; authors/admins can delete comments
- 🏷️ **Tags & Search** — tag posts and search/filter by keyword or tag
- 👤 **Author Profiles** — view all posts by a given author
- 🛡️ **Admin Panel** — admins can manage/delete any post, comment, or user
- 📱 **Responsive UI** — clean, component-based React frontend

## 🏗️ Tech Stack

| Layer     | Technology                                   |
|-----------|-----------------------------------------------|
| Frontend  | React.js (Vite), React Router, Axios, Context API |
| Backend   | Spring Boot 3, Spring Security, Spring Data MongoDB |
| Database  | MongoDB                                       |
| Auth      | JWT (JSON Web Tokens)                         |

## 📁 Project Structure

```
blog-app/
├── backend/     → Spring Boot REST API
└── frontend/    → React.js client
```

See `backend/README.md` and `frontend/README.md` for service-specific setup.

## 🚀 Quick Start

### Prerequisites
- Java 17+, Maven
- Node.js 18+
- MongoDB running locally (or an Atlas connection string)

### 1. Backend
```bash
cd backend
# edit src/main/resources/application.properties if needed
mvn spring-boot:run
```
API runs on `http://localhost:8080`

### 2. Frontend
```bash
cd frontend
npm install
npm run dev
```
App runs on `http://localhost:5173`

## 🔑 Default Roles

New users register with `USER` role. To create an `ADMIN`, register normally then update that user's `roles` field in MongoDB to include `ADMIN`, e.g.:

```js
db.users.updateOne({ email: "admin@example.com" }, { $set: { roles: ["ADMIN", "USER"] } })
```

## 📡 Key API Endpoints

| Method | Endpoint                  | Access        | Description               |
|--------|----------------------------|---------------|----------------------------|
| POST   | `/api/auth/register`       | Public        | Register a new user       |
| POST   | `/api/auth/login`          | Public        | Login, returns JWT        |
| GET    | `/api/posts`                | Public        | List published posts      |
| GET    | `/api/posts/{id}`           | Public        | Get single post           |
| POST   | `/api/posts`                | USER/ADMIN    | Create post                |
| PUT    | `/api/posts/{id}`           | Author/ADMIN  | Update post                |
| DELETE | `/api/posts/{id}`           | Author/ADMIN  | Delete post                |
| GET    | `/api/posts/{id}/comments`  | Public        | List comments on a post   |
| POST   | `/api/posts/{id}/comments`  | USER/ADMIN    | Add comment                |
| DELETE | `/api/comments/{id}`        | Author/ADMIN  | Delete comment             |
| GET    | `/api/users/me`             | Authenticated | Get current profile       |
| GET    | `/api/admin/users`          | ADMIN         | List all users             |

## 📄 License
MIT
