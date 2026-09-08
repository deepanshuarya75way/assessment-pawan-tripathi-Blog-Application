import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <Link to="/" className="brand">BlogSphere</Link>

      <div className="nav-links">
        <Link to="/">Home</Link>
        {user && <Link to="/create">Write</Link>}
        {isAdmin && <Link to="/admin">Admin</Link>}

        {user ? (
          <div className="nav-user">
            <Link to="/profile">{user.name}</Link>
            <button onClick={handleLogout} className="btn-link">Logout</button>
          </div>
        ) : (
          <div className="nav-user">
            <Link to="/login">Login</Link>
            <Link to="/register" className="btn-primary-sm">Sign Up</Link>
          </div>
        )}
      </div>
    </nav>
  );
}
