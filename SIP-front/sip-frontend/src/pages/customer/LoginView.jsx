import { useEffect, useState } from "react"
import AuthLayout from "../../components/layouts/AuthLayout.jsx"
import Input from "../../components/ui/Input.jsx"
import Button from "../../components/ui/Button.jsx"
import AuthTabs from "../../components/ui/AuthTabs.jsx"
import ErrorMessage from "../../components/ui/ErrorMessage.jsx"
import { loginCustomer, loginEmployee } from "../../services/authService.js";
import { useAuth } from "../../contex/AuthContext.jsx";
import { Link, useNavigate } from "react-router-dom";
import { required, pattern, minLength, REGEX } from "../../utils/validators";
import useForm from "../../hooks/useForm.js";

export default function LoginCustomer() {
  const [mode, setMode] = useState("customer")
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const { login } = useAuth();
  const [showPassword, setShowPassword] = useState(false);
  // Valores dinamicos para los formularios
  const FORM_CONFIG = {
    customer: {
      fields: [
        { name: "username", placeholder: "Phone / Username", type: "text" },
        { name: "password", placeholder: "Password", type: "password" }
      ],
      validation: {
        username: [required],
        password: [required, minLength(6)]
      }
    },

    employee: {
      fields: [
        { name: "username", placeholder: "Username", type: "text" },
        { name: "password", placeholder: "Password", type: "password" },
        { name: "establishmentCode", placeholder: "Establishment code", type: "text" }
      ],
      validation: {
        username: [required],
        password: [required, minLength(6)],
        establishmentCode: [required]
      }
    }
  };
  const getInitialValues = (mode) => {
    const values = {};
    FORM_CONFIG[mode].fields.forEach(f => {
      values[f.name] = "";
    });
    return values;
  };
  const config = FORM_CONFIG[mode];
  const { values, errors, handleChange, validateForm } = useForm(
    getInitialValues(mode),
    config.validation
  );
  const navigate = useNavigate();
  

  const handleLoginEmployee = async () => {

    if (!validateForm()) return;

    setError(null);
    setLoading(true);

    try {

      const res = await loginEmployee(values);

      login(res.token);

    } catch (e) {
      console.log(e);
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };
  const handleLoginCustomer = async () => {
    if (!validateForm()) return;
    console.log("entra");

    try {

      const res = await loginCustomer(values);

      login(res.token);

    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <AuthLayout title="Welcome">
      <AuthTabs value={mode} onChange={setMode} />

      <ErrorMessage>{error}</ErrorMessage>

      {mode === "customer" && (
        <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
          <Input 
          placeholder="Phone / Username" 
          value={values.username}
          onChange={handleChange("username")}
          error={errors.username}
          />
          <div style={{ position: "relative" }}>
                          <Input
                            type={showPassword ? "text" : "password"}
                            value={values.password}
                            placeholder="Password"
                            onChange={handleChange("password")}
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
                        
          </div>

          
          
            <p className="login__register"> 
              ¿No tienes cuenta?{" "}
              <Link to="/register" style={{ color: "#11f" }}>
                Crear cuenta
              </Link>
            </p> 
          <Button loading={loading} onClick={handleLoginCustomer}>
            Login as customer
          </Button>
        </div>
        
      )}

      {mode === "employee" && (
        <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
          <Input
            placeholder="Username"
            value={values.username}
            onChange={handleChange("username")}
            error={errors.username}
          />

          <div style={{ position: "relative" }}>
                          <Input
                            type={showPassword ? "text" : "password"}
                            value={values.password}
                            placeholder="Password"
                            onChange={handleChange("password")}
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
                        
          </div>

          <Input
            placeholder="Establishment code"
            value={values.establishmentCode}
            onChange={handleChange("establishmentCode")}
            error={errors.establishmentCode}
          />
            <p className="login__register"> 
              ¿Tienes un negocio?{" "}{" "}
              <Link to="/create-establishment" style={{ color: "#11f" }}>
                crear establecimiento
              </Link>
            </p>
          <Button loading={loading} onClick={handleLoginEmployee}>
            Login as employee
          </Button>
        </div>
      )}
      
    </AuthLayout>
  )
}