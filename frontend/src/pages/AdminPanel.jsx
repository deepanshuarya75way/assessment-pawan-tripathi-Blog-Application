import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { adminDeleteUser, fetchAllUsers } from '../api/endpoints';

export default function AdminPanel() {
  const { user: currentUser } = useAuth();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadUsers = () => {
    setLoading(true);
    fetchAllUsers()
      .then(({ data }) => setUsers(data))
      .catch(() => setError('Could not load users.'))
      .finally(() => setLoading(false));
  };

  useEffect(loadUsers, []);

  const handleDelete = async (id) => {
    if (id === currentUser.id) {
      alert("You can't delete your own account here.");
      return;
    }
    if (!window.confirm('Delete this user permanently?')) return;
    try {
      await adminDeleteUser(id);
      setUsers((prev) => prev.filter((u) => u.id !== id));
    } catch (err) {
      setError('Could not delete user.');
    }
  };

  return (
    <div className="page">
      <h1>Admin Panel</h1>
      <p className="muted">Manage registered users. Only accounts with the ADMIN role can view this page.</p>

      {error && <p className="error">{error}</p>}
      {loading ? (
        <p className="muted">Loading users...</p>
      ) : (
        <table className="admin-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Roles</th>
              <th>Joined</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td>{u.name}</td>
                <td>{u.email}</td>
                <td>{u.roles.join(', ')}</td>
                <td>{new Date(u.createdAt).toLocaleDateString()}</td>
                <td>
                  <button onClick={() => handleDelete(u.id)} className="btn-danger-sm">Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
