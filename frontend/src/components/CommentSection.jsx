import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { addComment, deleteComment, fetchComments } from '../api/endpoints';

export default function CommentSection({ postId }) {
  const { user, isAdmin } = useAuth();
  const [comments, setComments] = useState([]);
  const [text, setText] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadComments = async () => {
    setLoading(true);
    try {
      const { data } = await fetchComments(postId);
      setComments(data);
    } catch (err) {
      setError('Could not load comments.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadComments();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [postId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!text.trim()) return;
    try {
      await addComment(postId, { content: text.trim() });
      setText('');
      loadComments();
    } catch (err) {
      setError('Could not post comment.');
    }
  };

  const handleDelete = async (commentId) => {
    if (!window.confirm('Delete this comment?')) return;
    try {
      await deleteComment(commentId);
      setComments((prev) => prev.filter((c) => c.id !== commentId));
    } catch (err) {
      setError('Could not delete comment.');
    }
  };

  return (
    <section className="comments">
      <h3>Comments ({comments.length})</h3>

      {user ? (
        <form onSubmit={handleSubmit} className="comment-form">
          <textarea
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="Add a comment..."
            rows={3}
          />
          <button type="submit" className="btn-primary-sm">Post</button>
        </form>
      ) : (
        <p className="muted">Log in to join the discussion.</p>
      )}

      {error && <p className="error">{error}</p>}
      {loading ? (
        <p className="muted">Loading comments...</p>
      ) : (
        <ul className="comment-list">
          {comments.map((c) => (
            <li key={c.id} className="comment">
              <div className="comment-header">
                <strong>{c.authorName}</strong>
                <span className="muted"> · {new Date(c.createdAt).toLocaleDateString()}</span>
              </div>
              <p>{c.content}</p>
              {(isAdmin || user?.id === c.authorId) && (
                <button onClick={() => handleDelete(c.id)} className="btn-link small">
                  Delete
                </button>
              )}
            </li>
          ))}
          {comments.length === 0 && !loading && <p className="muted">No comments yet.</p>}
        </ul>
      )}
    </section>
  );
}
