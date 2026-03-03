import { Navigate } from "react-router-dom";
import { useAuth } from "../contex/AuthContext";

export default function PublicRoute({ children }) {
  const { isAuthenticated, user } = useAuth();

  if (isAuthenticated) {
    if (user?.role === "CUSTOMER") {
      return <Navigate to="/customer" replace />;
    }

    if (user?.role === "SELLER" || user?.role === "ADMIN") {
      return <Navigate to="/staff" replace />;
    }
  }

  return children;
}