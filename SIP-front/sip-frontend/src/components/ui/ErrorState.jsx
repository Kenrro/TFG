export default function ErrorState({ message, onRetry }) {
  return (
    <div className="error-state">
      <h3>Error</h3>
      <p>{message}</p>

      <button onClick={onRetry}>
        Reintentar
      </button>
    </div>
  );
}