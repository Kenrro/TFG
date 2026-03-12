import { useState } from "react";
import { useNavigate } from "react-router-dom";
import AppLayout from "../../components/layouts/AppLayout";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";
import "../../styles/scan.css";
import { joinStablishment } from "../../services/stablishmentService";
import QRCode from "react-qr-code";
import QrScanner from "../../components/QR/QrScanner";

export default function JoinEstablishmentView() {

  const [code, setCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);
  const [type, setType] = useState(null);

  const navigate = useNavigate();
  const handleScan = (value) => {
    if (!value) return;

    try {

      const parsed = JSON.parse(value);

      if (parsed.stablishmentCode) {

        setCode(parsed.stablishmentCode);

      }

    } catch {

      console.log("QR inválido");
      setType("error");
      setMessage("Invalid QR code.");

    }

  };
  const handleJoin = async () => {

    setLoading(true);
    setMessage(null);

    try {

      await joinStablishment(code);

      setType("success");
      setMessage("Successfully joined the establishment 🎉");

      setTimeout(() => {
        navigate("/customer");
      }, 1500);

    } catch (err) {

      if (err.status === 404) {
        setType("error");
        setMessage("Establishment not found");
      }

      else if (err.status === 409) {
        setType("error");
        setMessage("You are already registered in this establishment");
      }

      else {
        setType("error");
        setMessage("Something went wrong");
      }

    } finally {
      setLoading(false);
    }

  };

  return (
    <AppLayout>

      <div className="scan-container">

        <h2 className="scan-title">
          Join establishment
        </h2>

        <QrScanner onScan={handleScan} />

        <p className="scan-divider">
          or enter the code manually
        </p>

        <Input
          placeholder="Enter establishment code"
          value={code}
          onChange={(v) => setCode(v)}
        />

        <Button
          className="scan-submit"
          loading={loading}
          onClick={handleJoin}
          disabled={!code}
        >
          Join
        </Button>

        {message && (
          <p className={`scan-message ${type}`}>
            {message}
          </p>
        )}

      </div>

    </AppLayout>
  );
}