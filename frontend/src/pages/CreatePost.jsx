import { useNavigate } from 'react-router-dom';
import PostForm from '../components/PostForm';
import { createPost } from '../api/endpoints';

export default function CreatePost() {
  const navigate = useNavigate();

  const handleSubmit = async (payload) => {
    const { data } = await createPost(payload);
    navigate(`/posts/${data.id}`);
  };

  return (
    <div className="page">
      <h1>Write a New Post</h1>
      <PostForm onSubmit={handleSubmit} submitLabel="Publish" />
    </div>
  );
}
