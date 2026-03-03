import { useNavigate } from "react-router-dom";
import "../../styles/layout.css";

export default function SideDrawer({ open, onClose }) {
  const navigate = useNavigate();

  if (!open) return null;

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
    </div>
  );
}