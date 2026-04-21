import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../../styles/RegisterCustomer.css";
import { registerCustomer } from "../../services/authService";
import Input from "../../components/ui/Input";

export default function RegisterCustomer() {
  const [form, setForm] = useState({
    username: "",
    name: "",
    lastname: "",
    password: ""
  });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [confirmPassword, setConfirmPassword] = useState("");
  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };
  const navigate = useNavigate();
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    if (form.password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }
    setLoading(true);

    try {
        await registerCustomer(form);

        // redirigir a login
        navigate("/login");
    } catch (err) {
        setError(err.message);
    } finally {
        setLoading(false);
    }
    };

  return (
    <div className="register-page">
      <form className="register-card" onSubmit={handleSubmit}>
        <h2 className="title">Crear cuenta</h2>

        <input
          name="username"
          placeholder="Teléfono / usuario"
          value={form.username}
          onChange={handleChange}
          required
        />

        <input
          name="name"
          placeholder="Nombre"
          value={form.name}
          onChange={handleChange}
          required
        />

        <input
          name="lastname"
          placeholder="Apellido"
          value={form.lastname}
          onChange={handleChange}
          required
        />

        <div style={{ position: "relative" }}>
                <Input
                  type={showPassword ? "text" : "password"}
                  placeholder="Password"
                  onChange={(value) =>
                    setForm({ ...form, password: value })
                  }
                  required
                />
        
                <span
                  onClick={() => setShowPassword(!showPassword)}
                  style={{
                    position: "absolute",
                    right: 10,
                    top: "50%",
                    transform: "translateY(-50%)",
                    cursor: "pointer",
                    fontSize: "14px"
                  }}
                >
                  {showPassword ? "🙈" : "👁"}
                </span>
              </div>
              <div style={{ position: "relative" }}>
              <Input
                type={showConfirmPassword ? "text" : "password"}
                placeholder="Repeat password"
                onChange={(v) => setConfirmPassword(v)}
                required
              />
        
              <span
                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                style={{
                  position: "absolute",
                  right: 10,
                  top: "50%",
                  transform: "translateY(-50%)",
                  cursor: "pointer",
                  fontSize: "14px"
                }}
              >
                {showConfirmPassword ? "🙈" : "👁"}
              </span>
            </div>
            {confirmPassword && (
              <span style={{
                fontSize: "12px",
                color: form.password === confirmPassword ? "green" : "red"
              }}>
                {form.password === confirmPassword
                  ? "Passwords match"
                  : "Passwords do not match"}
              </span>
            )}

      {error && <p className="auth-error">{error}</p>}
      
        <p className="login__register"> 
              Ya tienes una cuenta{" "}
              <Link to="/login" style={{ color: "#11f" }}>
                Iniciar sesión
              </Link>
          </p> 
        <button className="auth-button" disabled={loading}>
        {loading ? "Creando..." : "Crear cuenta"}
        </button>
      </form>
    </div>
  );
}