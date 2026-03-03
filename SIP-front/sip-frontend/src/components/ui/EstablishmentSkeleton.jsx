import "../../styles/customer.css";

export default function EstablishmentSkeleton() {
  return (
    <div className="est-card skeleton">
      <div className="skeleton-image" />
      <div className="skeleton-content">
        <div className="skeleton-line short" />
        <div className="skeleton-line" />
        <div className="skeleton-line shorter" />
      </div>
    </div>
  );
}