import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { fetchPostsByAuthor, updateBio } from '../api/endpoints';
import PostCard from '../components/PostCard';

export default function Profile() {
  const { user } = useAuth();
  const [posts, setPosts] = useState([]);
  const [bio, setBio] = useState('');
  const [editingBio, setEditingBio] = useState(false);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!user) return;
    fetchPostsByAuthor(user.id, { size: 20 }).then(({ data }) => setPosts(data.content));
  }, [user]);

  const handleSaveBio = async () => {
    setSaving(true);
    try {
      await updateBio(bio);
      setEditingBio(false);
    } finally {
      setSaving(false);
    }
  };

  if (!user) return null;

  return (
    <div className="page">
      <div className="profile-header">
        <h1>{user.name}</h1>
        <p className="muted">{user.email}</p>
        <p className="muted">Roles: {user.roles.join(', ')}</p>

        {editingBio ? (
          <div className="bio-edit">
            <textarea value={bio} onChange={(e) => setBio(e.target.value)} rows={3} placeholder="Tell readers about yourself..." />
            <div className="post-actions">
              <button onClick={handleSaveBio} className="btn-primary-sm" disabled={saving}>
                {saving ? 'Saving...' : 'Save'}
              </button>
              <button onClick={() => setEditingBio(false)} className="btn-link">Cancel</button>
            </div>
          </div>
        ) : (
          <button onClick={() => setEditingBio(true)} className="btn-link">Edit bio</button>
        )}
      </div>

      <h2>Your Posts</h2>
      <div className="post-grid">
        {posts.map((post) => <PostCard key={post.id} post={post} />)}
      </div>
      {posts.length === 0 && <p className="muted">You haven't written any posts yet.</p>}
    </div>
  );
}
