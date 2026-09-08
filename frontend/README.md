# Frontend — React (Vite)

## Stack
- React 18 + Vite
- React Router v6
- Axios
- Context API for auth state

## Setup

```bash
npm install
cp .env.example .env   # adjust VITE_API_BASE_URL if your backend runs elsewhere
npm run dev
```

App runs on `http://localhost:5173` and expects the backend at `http://localhost:8080/api` by default.

## Structure

```
src/
├── api/            # axios instance + endpoint functions
├── components/     # Navbar, PostCard, PostForm, CommentSection, route guards
├── context/        # AuthContext (login/register/logout, current user)
├── pages/          # Home, Login, Register, PostDetail, CreatePost, EditPost, Profile, AuthorPage, AdminPanel
├── styles/          # App.css
├── App.jsx          # routes
└── main.jsx          # entry point
```

## Notes
- JWT is stored in `localStorage` and attached to every request via an Axios interceptor.
- `PrivateRoute` guards authenticated-only pages; `AdminRoute` additionally checks for the `ADMIN` role (mirrors the backend's RBAC).
- A 401 response anywhere clears the stored session automatically.
