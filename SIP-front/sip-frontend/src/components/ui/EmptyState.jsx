import "../../styles/customer.css";

export default function EmptyState({ title, description, actionLabel, onAction }) {
  return (
    <div className="empty-state fade-in">
        <div className="empty-icon">
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="80"
                height="80"
            >
                <path
                d="M3 9l9-6 9 6v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"
                fill="none"
                stroke="#1E3A8A"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
                />
                <path
                d="M9 22V12h6v10"
                fill="none"
                stroke="#1E3A8A"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
                />
            </svg>
            </div>

      <h3>{title}</h3>
      <p>{description}</p>

      {actionLabel && (
        <button className="empty-action" onClick={onAction}>
          {actionLabel}
        </button>
      )}
    </div>
  );
}