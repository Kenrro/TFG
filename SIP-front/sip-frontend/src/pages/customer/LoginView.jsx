import { useEffect, useState } from "react"
import AuthLayout from "../../components/layouts/AuthLayout.jsx"
import Input from "../../components/ui/Input.jsx"
import Button from "../../components/ui/Button.jsx"
import AuthTabs from "../../components/ui/AuthTabs.jsx"
import ErrorMessage from "../../components/ui/ErrorMessage.jsx"
import { loginCustomer, loginEmployee } from "../../services/authService.js";
import { useAuth } from "../../contex/AuthContext.jsx";
import { useNavigate } from "react-router-dom";

export default function LoginCustomer() {
  const [mode, setMode] = useState("customer")
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const { login } = useAuth();
  const [form, setForm] = useState({
    username: "",
    password: "",
    establishmentCode: ""
  });
  const navigate = useNavigate();
    // limpiar el mode
    useEffect(() => {
    setForm({
      username: "",
      password: "",
      establishmentCode: ""
    });
  }, [mode]);

  const handleLoginEmployee = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const res = await loginEmployee(form);
      login(res.token);

    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }
  const handleChange = (field) => (value) => {
  setForm(prev => ({ ...prev, [field]: value }));
};
  const handleLoginCustomer = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const res = await loginCustomer(
        {
          username: form.username,
          password: form.password
        }
      );
      login(res.token);

    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout title="Welcome">
      <AuthTabs value={mode} onChange={setMode} />

      <ErrorMessage>{error}</ErrorMessage>

      {mode === "customer" && (
        <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
          <Input placeholder="Phone / Username" onChange={handleChange("username")}/>
          <Input type="password" placeholder="Password" onChange={handleChange("password")}/>
          <p className="login__register"> 
            ¿No tienes cuenta?{" "}
            <span style={{color: "#11f", cursor: "pointer"}} onClick={() => navigate("/register")}>
              Crear cuenta
            </span>
          </p>
          <Button loading={loading} onClick={handleLoginCustomer}>
            Login as customer
          </Button>
        </div>
        
      )}

      {mode === "employee" && (
        <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
          <Input placeholder="Username" onChange={handleChange("username")}/>
          <Input type="password" placeholder="Password" onChange={handleChange("password")}/>
          <Input placeholder="Establishment code" onChange={handleChange("establishmentCode")}/>
          <p className="login__register"> 
            ¿Tienes un negocio?{" "}
            <span style={{color: "#11f", cursor: "pointer"}} onClick={() => navigate("/create-establishment")}>
              Crear establecimiento
            </span>
          </p>
          <Button loading={loading} onClick={handleLoginEmployee}>
            Login as employee
          </Button>
        </div>
      )}
      
    </AuthLayout>
  )
}