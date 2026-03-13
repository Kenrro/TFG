import { useNavigate } from "react-router-dom";
import { useAuth } from "../../contex/AuthContext";
import "../../styles/layout.css";

export default function SideDrawer({ open, onClose, items = [] }) {

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

      {items.map((item) => (
        <button
          key={item.path}
          className="drawer-item"
          onClick={() => {
            navigate(item.path);
            onClose();
          }}
        >
          <span>{item.icon}</span>
          {item.label}
        </button>
      ))}

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