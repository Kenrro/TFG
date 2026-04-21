import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AppLayout from "../../components/layouts/AppLayout";
import Button from "../../components/ui/Button";
import "../../styles/scan.css";
import { redeemProduct } from "../../services/transactionService";
import QRCode from "react-qr-code";
import BackArrow from "../../components/ui/BackArrow";

export default function CustomerRedeemView() {

  const [transaction, setTransaction] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const { state } = useLocation();

  useEffect(() => {

    const createTransaction = async () => {
      try {
        console.log("Redeeming product with data:", state.incentive);
        const data = await redeemProduct(state.incentive.incentiveResponseDto.id, state.incentive.productResponsetDto.id, state.incentive.productResponsetDto.stablishmentCode);
        console.log(data);
        setTransaction(data);

      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    createTransaction();

  }, []);

  if (loading) {
    return (
      <AppLayout>
        <div className="redeem-loading">
          Generating code...
        </div>
      </AppLayout>
    );
  }

  return (
    <AppLayout>
      <BackArrow></BackArrow>
      <div className="redeem-container">

        <h2 className="redeem-title">
          Show this code to the employee
        </h2>
        <div className="qr-box">
          <QRCode
            value={JSON.stringify(transaction) || "No transaction ID"}
            size={256}
            bgColor="#ffffff"
            fgColor="#000000"
            level="H"
          />
        </div>

        <div className="redeem-code">
          {transaction?.id}
        </div>

        <p className="redeem-help">
          The employee will scan this code to complete the redemption.
        </p>


      </div>

    </AppLayout>
  );
}