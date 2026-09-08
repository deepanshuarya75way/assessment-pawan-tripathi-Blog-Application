import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import PostForm from '../components/PostForm';
import { fetchPost, updatePost } from '../api/endpoints';

export default function EditPost() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchPost(id)
      .then(({ data }) => setPost(data))
      .catch(() => setError('Could not load post.'))
      .finally(() => setLoading(false));
  }, [id]);

  const handleSubmit = async (payload) => {
    await updatePost(id, payload);
    navigate(`/posts/${id}`);
  };

  if (loading) return <p className="muted page">Loading...</p>;
  if (error) return <p className="error page">{error}</p>;

  return (
    <div className="page">
      <h1>Edit Post</h1>
      <PostForm initialValues={post} onSubmit={handleSubmit} submitLabel="Save Changes" />
    </div>
  );
}
