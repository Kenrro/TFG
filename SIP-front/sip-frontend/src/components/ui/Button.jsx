import "../../styles/ui.css"

export default function Button({ children, loading, ...props }) {
  return (
    <button
      className={`btn ${loading ? "loading" : ""}`}
      disabled={loading}
      {...props}
    >
      {loading ? <span className="spinner" /> : children}
    </button>
  )
}