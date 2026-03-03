import AppLayout from "../../components/layouts/AuthLayout";
import { useAuth } from "../../contex/AuthContext";

export default function StaffDashboard() {
  const { user, logout } = useAuth();

  return (
    <AppLayout>
      <h2>Staff Panel</h2>
      <p>Welcome {user?.name}</p>
      <p>Role: {user?.role}</p>
      <p>Role: {user?.username}</p>

      <button onClick={logout}>Logout</button>
    </AppLayout>
  );
}