import "../../styles/errorModal.css";

export default function ErrorModal({ message, onClose }) {
  return (
    <div className="error-overlay" onClick={onClose}>
      <div
        className="error-card"
        onClick={(e) => e.stopPropagation()}
      >
        <h3>⚠️ Error</h3>

        <p className="error-message">
          {message || "Something went wrong"}
        </p>

        <button className="error-button" onClick={onClose}>
          Accept
        </button>
      </div>
    </div>
  );
}