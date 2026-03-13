import { useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";
import QRCode from "react-qr-code";
import "../../styles/staffTransactions.css";
import { createGivePointsTransaction } from "../../services/transactionService";

export default function StaffGivePointsView() {

  const [amount, setAmount] = useState("");
  const [loading, setLoading] = useState(false);
  const [transactionId, setTransactionId] = useState(null);
  const [error, setError] = useState(null);

  const handleCreateTransaction = async () => {

    if (!amount || Number(amount) <= 0) {
      setError("Enter a valid amount");
      return;
    }

    setLoading(true);
    setError(null);

    try {

      const res = await createGivePointsTransaction({
        amountSpent: Number(amount)
      });
      setTransactionId(res.id);

    } catch (err) {

      console.error(err);
      setError("Could not create transaction");

    } finally {

      setLoading(false);

    }
  };

  return (
    <AppLayout>

      <div className="staff-transaction-container">

        {!transactionId && (
          <>
            <h2 className="staff-title">
              Give points
            </h2>

            <p className="staff-subtitle">
              Enter the amount spent by the customer
            </p>

            <Input
              type="number"
              placeholder="Amount spent (€)"
              value={amount}
              onChange={(v) => setAmount(v)}
            />

            {error && (
              <p className="staff-error">{error}</p>
            )}

            <div className="staff-actions">

            <Button
                className="staff-btn-primary"
                loading={loading}
                onClick={handleCreateTransaction}
            >
                Generate QR
            </Button>

</div>
          </>
        )}

        {transactionId && (
          <div className="qr-result">

            <h2>Scan this QR</h2>

            <div className="qr-box">
              <QRCode value={JSON.stringify({ id: transactionId })} size={220} />
            </div>

            <p className="transaction-id">
              Code: {transactionId}
            </p>

            <div className="staff-actions">

            <Button
                className="staff-btn-secondary"
                onClick={() => {
                    setTransactionId(null);
                    setAmount("");
                }}
                >
                New transaction
                </Button>

</div>

          </div>
        )}

      </div>

    </AppLayout>
  );
}