import api from './axios';

// ---- Auth ----
export const registerUser = (data) => api.post('/auth/register', data);
export const loginUser = (data) => api.post('/auth/login', data);

// ---- Posts ----
export const fetchPosts = (params) => api.get('/posts', { params });
export const fetchPost = (id) => api.get(`/posts/${id}`);
export const fetchPostsByAuthor = (authorId, params) => api.get(`/posts/author/${authorId}`, { params });
export const createPost = (data) => api.post('/posts', data);
export const updatePost = (id, data) => api.put(`/posts/${id}`, data);
export const deletePost = (id) => api.delete(`/posts/${id}`);

// ---- Comments ----
export const fetchComments = (postId) => api.get(`/posts/${postId}/comments`);
export const addComment = (postId, data) => api.post(`/posts/${postId}/comments`, data);
export const deleteComment = (commentId) => api.delete(`/comments/${commentId}`);

// ---- Users ----
export const fetchCurrentUser = () => api.get('/users/me');
export const fetchUser = (id) => api.get(`/users/${id}`);
export const updateBio = (bio) => api.patch('/users/me/bio', { bio });

// ---- Admin ----
export const fetchAllUsers = () => api.get('/admin/users');
export const adminDeleteUser = (id) => api.delete(`/admin/users/${id}`);
