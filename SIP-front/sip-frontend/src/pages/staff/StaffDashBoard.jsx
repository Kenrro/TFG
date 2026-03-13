import AppLayout from "../../components/layouts/AppLayout";
import Button from "../../components/ui/Button";
import QRCode from "react-qr-code";
import { useAuth } from "../../contex/AuthContext";
import { decodeToken } from "../../utils/jwt";
import "../../styles/staff.css";
import { useNavigate } from "react-router-dom";
export default function StaffDashboard() {

  const { token } = useAuth();
  const user = decodeToken(token);
  const navigate = useNavigate();

  const stablishmentCode = user?.establishmentCode || "UNKNOWN";

  return (
    <AppLayout>

      <div className="staff-container">

        <h2 className="staff-title">
          Establishment QR
        </h2>

        <div className="staff-qr-card">

          <QRCode
            value={JSON.stringify({ stablishmentCode })}
            size={220}
          />

          <p className="staff-code">
            {stablishmentCode}
          </p>

        </div>

        <div className="staff-actions">

        <button className="staff-btn give"
        onClick={() => navigate("/staff/give-points")}
        >
          💰 Give Points
        </button>

        <button className="staff-btn scan"
        onClick={() => navigate("/staff/redeem")}>
          📷 Scan Customer QR
        </button>

      </div>

      </div>

    </AppLayout>
  );
}