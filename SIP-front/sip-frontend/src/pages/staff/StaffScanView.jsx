import { useState } from "react";
import { useNavigate } from "react-router-dom";
import AppLayout from "../../components/layouts/AppLayout";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";
import "../../styles/scan.css";
import { updateRedeemTransaction } from "../../services/transactionService";
import QrScanner from "../../components/QR/QrScanner";
import ErrorModal from "./ErrorModal";
import BackArrow from "../../components/ui/BackArrow";


export default function CustomerScanView() {

  const [error, setError] = useState(null);

  const [code, setCode] = useState("");
  const [message, setMessage] = useState(null);
  const [type, setType] = useState(null);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleScan = (value) => {
    if (!value) return;

    try {

      const parsed = JSON.parse(value);

      if (parsed.id) {

        setCode(parsed.id);

      }

    } catch {

      console.log("QR inválido");
      setType("error");
      setMessage("Invalid QR code.");

    }

  };
  const handleManualSubmit = async () => {

    setLoading(true);
    setMessage(null);

    try {

      const response = await updateRedeemTransaction(code);
      console.log(response);
      setType("success");
      setMessage("Product redeemed successfully 🎉");

      setTimeout(() => {
        navigate(-1); // vuelve al establecimiento
      }, 1500);

    } catch (error) {
      console.error(error);
      if (error.status === 404) {
        setType("error");
        setMessage("Code not found.");
      }

      else if (error.status === 409) {
        setType("error");
        setMessage("This code was already used.");
      }

      else if (error.status === 410) {
        setType("error");
        setMessage("This code has expired.");
      }
      
      else {
        setType("error");
        setMessage("Something went wrong.");
      }

    } finally {
      setLoading(false);
    }
  };

  return (
    <AppLayout>
      <BackArrow></BackArrow>
      <div className="scan-container">

        <h2 className="scan-title">
          Scan QR
        </h2>

        <QrScanner onScan={handleScan} />

        <p className="scan-divider">
          or enter the code manually
        </p>

        <Input
          placeholder="Enter code"
          value={code}
          onChange={(v) => setCode(v)}
        />

        {message && (
          <p className={`scan-message ${type}`}>
            {message}
          </p>
        )}

        <Button
          className="scan-submit"
          onClick={handleManualSubmit}
          disabled={!code || loading}
        >
          🎟 Redeem Code
        </Button>

      </div>
        {error && (
        <ErrorModal
          message={error}
          onClose={() => setError(null)}
        />
    )}
    </AppLayout>
  );
}