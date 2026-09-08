import { Link } from 'react-router-dom';

export default function PostCard({ post }) {
  const date = new Date(post.createdAt).toLocaleDateString(undefined, {
    year: 'numeric', month: 'short', day: 'numeric'
  });

  return (
    <article className="post-card">
      {post.status === 'DRAFT' && <span className="badge-draft">Draft</span>}
      <h2>
        <Link to={`/posts/${post.id}`}>{post.title}</Link>
      </h2>
      <p className="post-excerpt">{post.excerpt}</p>
      <div className="post-meta">
        <Link to={`/authors/${post.authorId}`}>{post.authorName}</Link>
        <span> · {date}</span>
      </div>
      {post.tags?.length > 0 && (
        <div className="tags">
          {post.tags.map((tag) => (
            <Link key={tag} to={`/?tag=${encodeURIComponent(tag)}`} className="tag">
              #{tag}
            </Link>
          ))}
        </div>
      )}
    </article>
  );
}
