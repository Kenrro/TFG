import { useNavigate, useParams } from "react-router-dom";
import AppLayout from "../../components/layouts/AppLayout";
import PointsCounter from "../../components/ui/PointsCounter";
import IncentiveCard from "../../components/ui/IncentiveCard";
import "../../styles/establishment.css";
import default_image from "../../assets/image.png";
import { useLocation } from "react-router-dom";
import { use, useEffect, useState } from "react";
import { getWalletPoints } from "../../services/walletService";
import ErrorState from "../../components/ui/ErrorState";
import { getIncentives } from "../../services/incentiveService";
import EmptyState from "../../components/ui/EmptyState";
import IncentiveSkeleton from "../../components/ui/IncentiveSkeleton";
import ConfirmModal from "../../components/ui/ConfirmModal";
import { leaveStablishment } from "../../services/stablishmentService";
import Button from "../../components/ui/Button";

export default function CustomerEstablishmentView() {

  const { state } = useLocation();
  const establishment = state;
  const { code } = useParams();
  const [points, setPoints] = useState();
  const [error, setError] = useState(null);
  const [incentives, setIncentives] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showConfirm, setShowConfirm] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {

        const wallet = await getWalletPoints(code);
        setPoints(wallet.balance);

        const incentivesData = await getIncentives(code);

        setIncentives(incentivesData.incentives || []);

      } catch (err) {
        console.error(err);
        setError("Error loading establishment data");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [code]);
  
  
  const handleLeave = async () => {

    try {

      const response = await leaveStablishment(code);
      navigate("/customer");

    } catch (err) {

      console.error(err);

    }

  };

  if (error) {
      return (
        <ErrorState
          message={error}
          onRetry={() => window.location.reload()}
        />
      );
    }
  return (
    <AppLayout>

      <div className="establishment-hero">

        <img
            src={default_image}
            alt={establishment.name}
            className="establishment-image"
        />

        <h2 className="establishment-title">
            {establishment.name}
        </h2>

        </div>

        <div className="points-sticky">
        <PointsCounter points={points} />
        </div>
        <button
            className="scan-btn"
            onClick={() => navigate("/customer/scan")}
          >
            Scan QR to earn points
        </button>

        <div className="incentives-list">

          {loading && (
            <>
              <IncentiveSkeleton />
              <IncentiveSkeleton />
              <IncentiveSkeleton />
            </>
          )}

          {!loading && incentives.length === 0 && (
            <EmptyState message="No incentives available yet" />
          )}

          {!loading && incentives.length > 0 &&
            incentives.map(incentive => (
              <IncentiveCard
                key={incentive.incentiveResponseDto.id}
                incentive={incentive}
                userPoints={points}
              />
          ))}
          <Button
            onClick={() => navigate(-1)}
          >
            Back
          </Button>
          <div className="leave-container">

      <button
        className="leave-btn"
        onClick={() => setShowConfirm(true)}
      >
        Leave establishment
      </button>
          
    </div>

        </div>
        {showConfirm && (
          <ConfirmModal
            title="Leave establishment?"
            message="Are you sure you want to leave this establishment? You will lose access to your points and incentives."
            onConfirm={handleLeave}
            onCancel={() => setShowConfirm(false)}
          />
        )}

    </AppLayout>
  );
}