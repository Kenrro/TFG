import "../../styles/ui.css"

export default function PointsCounter({ points }) {

  return (
    <div className="points-counter">

      <span className="points-value">
        {points}
      </span>

      <span className="points-label">
        points
      </span>

    </div>
  );
}