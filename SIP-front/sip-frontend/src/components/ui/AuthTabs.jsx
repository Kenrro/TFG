import "../../styles/ui.css"

export default function AuthTabs({ value, onChange }) {
  return (
    <div className="auth-tabs">
      <button
        className={`auth-tab ${value === "customer" ? "active" : ""}`}
        onClick={() => onChange("customer")}
      >
        Customer
      </button>

      <button
        className={`auth-tab ${value === "employee" ? "active" : ""}`}
        onClick={() => onChange("employee")}
      >
        Employee
      </button>
    </div>
  )
}