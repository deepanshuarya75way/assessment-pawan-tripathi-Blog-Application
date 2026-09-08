import { useState } from 'react';

export default function PostForm({ initialValues, onSubmit, submitLabel }) {
  const [form, setForm] = useState({
    title: initialValues?.title || '',
    content: initialValues?.content || '',
    excerpt: initialValues?.excerpt || '',
    tags: initialValues?.tags?.join(', ') || '',
    status: initialValues?.status || 'PUBLISHED',
  });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!form.title.trim() || !form.content.trim()) {
      setError('Title and content are required.');
      return;
    }
    setSubmitting(true);
    try {
      await onSubmit({
        title: form.title.trim(),
        content: form.content.trim(),
        excerpt: form.excerpt.trim() || undefined,
        tags: form.tags.split(',').map((t) => t.trim()).filter(Boolean),
        status: form.status,
      });
    } catch (err) {
      setError(err.response?.data?.message || 'Something went wrong. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="post-form">
      {error && <p className="error">{error}</p>}

      <label>
        Title
        <input type="text" name="title" value={form.title} onChange={handleChange} required />
      </label>

      <label>
        Excerpt (optional — auto-generated from content if left blank)
        <input type="text" name="excerpt" value={form.excerpt} onChange={handleChange} />
      </label>

      <label>
        Content
        <textarea name="content" value={form.content} onChange={handleChange} rows={14} required />
      </label>

      <label>
        Tags (comma-separated)
        <input type="text" name="tags" value={form.tags} onChange={handleChange} placeholder="react, spring-boot, mongodb" />
      </label>

      <label>
        Status
        <select name="status" value={form.status} onChange={handleChange}>
          <option value="PUBLISHED">Published</option>
          <option value="DRAFT">Draft</option>
        </select>
      </label>

      <button type="submit" className="btn-primary" disabled={submitting}>
        {submitting ? 'Saving...' : submitLabel}
      </button>
    </form>
  );
}
