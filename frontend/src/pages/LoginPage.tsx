import { useState, type FormEvent } from 'react';
import { useNavigate, useLocation, Navigate } from 'react-router-dom';
import { Card, CardContent, TextField, Button, Alert, CircularProgress } from '@mui/material';
import { LockOutlined } from '@mui/icons-material';
import { useAuth } from '../auth/AuthContext';
import { extractMessage } from '../api/client';

export function LoginPage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as { from?: Location })?.from?.pathname ?? '/';

  const [username, setUsername] = useState('admin');
  const [password, setPassword] = useState('admin');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);


  if (user) return <Navigate to={from} replace />;

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await login(username, password);
      navigate(from, { replace: true });
    } catch (err) {
      const status = (err as { response?: { status?: number } }).response?.status;
      if (status === 401) {
        setError('Неверный логин или пароль');
      } else {
        setError(extractMessage(err));
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-100 to-slate-200">
      <Card className="w-[400px]">
        <CardContent className="p-8">
          <div className="flex flex-col items-center mb-6">
            <div className="w-12 h-12 rounded-full bg-sky-100 flex items-center justify-center mb-3">
              <LockOutlined className="text-sky-600" />
            </div>
            <h1 className="text-xl font-semibold text-gray-900">Travel Agency Admin</h1>
            <p className="text-sm text-gray-500 mt-1">Войдите в систему</p>
          </div>

          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <TextField
              label="Имя пользователя"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoFocus
              fullWidth
              size="small"
              required
            />
            <TextField
              label="Пароль"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              fullWidth
              size="small"
              required
            />
            {error && <Alert severity="error">{error}</Alert>}
            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={loading}
              fullWidth
            >
              {loading ? <CircularProgress size={24} color="inherit" /> : 'Войти'}
            </Button>
          </form>

          <p className="text-xs text-gray-400 mt-6 text-center">
            По умолчанию: admin / admin
          </p>
        </CardContent>
      </Card>
    </div>
  );
}
