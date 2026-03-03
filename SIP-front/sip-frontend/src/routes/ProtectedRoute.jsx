import { Navigate } from "react-router-dom";
import { useAuth } from "../contex/AuthContext";

export default function ProtectedRoute({ children, roles }) {
  const { user, isAuthenticated } = useAuth();

  // No logueado → login
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // Si se especifican roles y el usuario no está incluido
  if (roles && !roles.includes(user?.role)) {
    return <Navigate to="/login" replace />;
  }

  return children;
}