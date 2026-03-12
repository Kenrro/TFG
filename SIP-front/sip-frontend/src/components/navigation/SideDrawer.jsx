import { useNavigate } from "react-router-dom";
import { useAuth } from "../../contex/AuthContext";
import "../../styles/layout.css";

export default function SideDrawer({ open, onClose }) {
  const navigate = useNavigate();
  const { logout } = useAuth();

  if (!open) return null;

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="drawer" onClick={(e) => e.stopPropagation()}>
      <h3 className="drawer-title">Menú</h3>

      <button
        className="drawer-item"
        onClick={() => {
          navigate("/customer");
          onClose();
        }}
      >
        <span>🏪</span>
        Establecimientos
      </button>

      <button
        className="drawer-item"
        onClick={() => {
          navigate("/customer/settings");
          onClose();
        }}
      >
        <span>⚙</span>
        Configuración
      </button>

      <div className="drawer-spacer"></div>

      <button
        className="drawer-item drawer-logout"
        onClick={handleLogout}
      >
        <span>🚪</span>
        Cerrar sesión
      </button>
    </div>
  );
}