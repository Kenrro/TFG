import "../../styles/incentive.css";

export default function IncentiveSkeleton() {
  return (
    <div className="incentive-card skeleton">

      <div className="skeleton-image"></div>

      <div className="skeleton-content">
        <div className="skeleton-line title"></div>
        <div className="skeleton-line description"></div>

        <div className="skeleton-footer">
          <div className="skeleton-line points"></div>
          <div className="skeleton-button"></div>
        </div>
      </div>

    </div>
  );
}