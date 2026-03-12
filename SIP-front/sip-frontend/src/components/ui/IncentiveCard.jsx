import defaultImage from "../../assets/image.png";
import "../../styles/incentive.css";
import { useLocation, useNavigate } from "react-router-dom";

export default function IncentiveCard({ incentive, userPoints }) {

  const { incentiveResponseDto, productResponsetDto } = incentive;

  const canRedeem = userPoints >= incentiveResponseDto.pointsRequired;
  const navigate = useNavigate();

  return (
    <div className="incentive-card">

      <img
        src={defaultImage}
        alt={productResponsetDto.name}
        className="incentive-image"
      />

      <div className="incentive-content">

        <h4 className="incentive-title">
          {productResponsetDto.name}
        </h4>

        <p className="incentive-description">
          {productResponsetDto.description}
        </p>

        <div className="incentive-footer">

          <span className="points-required">
            {incentiveResponseDto.pointsRequired} pts
          </span>

          <button
            className="redeem-btn"
            disabled={!canRedeem}
            onClick={() => navigate("/customer/redeem", { state: { incentive }})}
          >
            Redeem
            </button>

        </div>

      </div>

    </div>
  );
}