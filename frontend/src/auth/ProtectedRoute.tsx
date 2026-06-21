import { Navigate, useLocation } from 'react-router-dom';
import { CircularProgress, Box } from '@mui/material';
import { useAuth } from './AuthContext';

interface Props {
  children: React.ReactNode;
  requiredPermission?: string;
}

export function ProtectedRoute({ children, requiredPermission }: Props) {
  const { user, loading, hasPermission } = useAuth();
  const location = useLocation();

  if (loading) {
    return (
      <Box className="flex items-center justify-center min-h-screen">
        <CircularProgress />
      </Box>
    );
  }

  if (!user) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  if (requiredPermission && !hasPermission(requiredPermission)) {
    return (
      <Box className="p-8 text-center">
        <h2 className="text-xl font-semibold text-gray-800 mb-2">Доступ запрещён</h2>
        <p className="text-gray-500">
          Для этой страницы нужно разрешение <code>{requiredPermission}</code>.
          Обратитесь к администратору.
        </p>
      </Box>
    );
  }

  return <>{children}</>;
}
