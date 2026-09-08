import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { deletePost, fetchPost } from '../api/endpoints';
import CommentSection from '../components/CommentSection';

export default function PostDetail() {
  const { id } = useParams();
  const { user, isAdmin } = useAuth();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetchPost(id)
      .then(({ data }) => setPost(data))
      .catch(() => setError('Post not found.'))
      .finally(() => setLoading(false));
  }, [id]);

  const canEdit = user && post && (isAdmin || user.id === post.authorId);

  const handleDelete = async () => {
    if (!window.confirm('Delete this post permanently?')) return;
    try {
      await deletePost(id);
      navigate('/');
    } catch (err) {
      setError('Could not delete post.');
    }
  };

  if (loading) return <p className="muted page">Loading...</p>;
  if (error) return <p className="error page">{error}</p>;
  if (!post) return null;

  return (
    <div className="page post-detail">
      {post.status === 'DRAFT' && <span className="badge-draft">Draft</span>}
      <h1>{post.title}</h1>
      <div className="post-meta">
        By <Link to={`/authors/${post.authorId}`}>{post.authorName}</Link>
        {' · '}
        {new Date(post.createdAt).toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' })}
      </div>

      {post.tags?.length > 0 && (
        <div className="tags">
          {post.tags.map((tag) => (
            <Link key={tag} to={`/?tag=${encodeURIComponent(tag)}`} className="tag">#{tag}</Link>
          ))}
        </div>
      )}

      {canEdit && (
        <div className="post-actions">
          <Link to={`/posts/${id}/edit`} className="btn-primary-sm">Edit</Link>
          <button onClick={handleDelete} className="btn-danger-sm">Delete</button>
        </div>
      )}

      <div className="post-content">
        {post.content.split('\n').map((para, i) => para.trim() && <p key={i}>{para}</p>)}
      </div>

      <CommentSection postId={id} />
    </div>
  );
}
