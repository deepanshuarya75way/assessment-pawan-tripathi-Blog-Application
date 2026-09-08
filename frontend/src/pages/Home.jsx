import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import PostCard from '../components/PostCard';
import { fetchPosts } from '../api/endpoints';

export default function Home() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchInput, setSearchInput] = useState(searchParams.get('search') || '');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const search = searchParams.get('search') || '';
  const tag = searchParams.get('tag') || '';

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    fetchPosts({ search: search || undefined, tag: tag || undefined, page, size: 6 })
      .then(({ data }) => {
        if (cancelled) return;
        setPosts(data.content);
        setTotalPages(data.totalPages);
      })
      .catch(() => !cancelled && setError('Could not load posts.'))
      .finally(() => !cancelled && setLoading(false));
    return () => { cancelled = true; };
  }, [search, tag, page]);

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    setSearchParams(searchInput ? { search: searchInput } : {});
  };

  return (
    <div className="page">
      <div className="hero">
        <h1>Welcome to BlogSphere</h1>
        <p>Read, write, and share ideas.</p>
        <form onSubmit={handleSearch} className="search-form">
          <input
            type="text"
            placeholder="Search posts..."
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
          />
          <button type="submit" className="btn-primary-sm">Search</button>
        </form>
        {tag && (
          <p className="muted">
            Filtering by tag: <strong>#{tag}</strong>
          </p>
        )}
      </div>

      {error && <p className="error">{error}</p>}
      {loading ? (
        <p className="muted">Loading posts...</p>
      ) : (
        <>
          <div className="post-grid">
            {posts.map((post) => <PostCard key={post.id} post={post} />)}
          </div>
          {posts.length === 0 && <p className="muted">No posts found.</p>}

          {totalPages > 1 && (
            <div className="pagination">
              <button disabled={page === 0} onClick={() => setPage((p) => p - 1)}>Previous</button>
              <span>Page {page + 1} of {totalPages}</span>
              <button disabled={page + 1 >= totalPages} onClick={() => setPage((p) => p + 1)}>Next</button>
            </div>
          )}
        </>
      )}
    </div>
  );
}
