import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchPostsByAuthor, fetchUser } from '../api/endpoints';
import PostCard from '../components/PostCard';

export default function AuthorPage() {
  const { authorId } = useParams();
  const [author, setAuthor] = useState(null);
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([fetchUser(authorId), fetchPostsByAuthor(authorId, { size: 20 })])
      .then(([userRes, postsRes]) => {
        setAuthor(userRes.data);
        setPosts(postsRes.data.content.filter((p) => p.status === 'PUBLISHED'));
      })
      .finally(() => setLoading(false));
  }, [authorId]);

  if (loading) return <p className="muted page">Loading...</p>;
  if (!author) return <p className="error page">Author not found.</p>;

  return (
    <div className="page">
      <div className="profile-header">
        <h1>{author.name}</h1>
        {author.bio && <p>{author.bio}</p>}
      </div>

      <h2>Posts by {author.name}</h2>
      <div className="post-grid">
        {posts.map((post) => <PostCard key={post.id} post={post} />)}
      </div>
      {posts.length === 0 && <p className="muted">No published posts yet.</p>}
    </div>
  );
}
