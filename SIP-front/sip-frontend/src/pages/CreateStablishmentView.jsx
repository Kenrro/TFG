import { useState } from "react";
import AuthLayout from "../components/layouts/AuthLayout.jsx";
import Input from "../components/ui/Input";
import Button from "../components/ui/Button";
import ErrorMessage from "../components/ui/ErrorMessage";
import { createEstablishment } from "../services/stablishmentService";
import { useNavigate } from "react-router-dom";
import { jsPDF } from "jspdf";

export default function CreateEstablishmentView() {
  const navigate = useNavigate();

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [created, setCreated] = useState(null);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [confirmPassword, setConfirmPassword] = useState("");
  const [form, setForm] = useState({
    name: "",
    address: "",
    description: "",
    adminUsername: "",
    adminPassword: "",
    adminName: "",
    adminLastname: ""
  });

  const handleChange = (field) => (value) => {
    setForm(prev => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async () => {
    setError(null);

    if (form.adminPassword !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    setLoading(true);

    try {
      const response = await createEstablishment(form);
      setCreated(response); 
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };
  const handleDownload = () => {
    if (!created) return;

    const doc = new jsPDF();

    // Título
    doc.setFontSize(18);
    doc.setTextColor(30, 58, 138); // Azul oscuro (#1E3A8A)
    doc.text("Establishment Created Successfully", 20, 20);

    // Línea separadora
    doc.setDrawColor(96, 165, 250); // Azul claro
    doc.line(20, 25, 190, 25);

    doc.setFontSize(12);
    doc.setTextColor(55, 65, 81); // Gris oscuro

    doc.text(`Name: ${created.name}`, 20, 40);
    doc.text(`Code: ${created.code}`, 20, 50);
    doc.text(`Address: ${created.address}`, 20, 60);
    doc.text(`Description: ${created.description}`, 20, 70);

    // Advertencia
    doc.setTextColor(245, 158, 11); // Amarillo acento
    doc.text(
      "IMPORTANT: Save this code securely. It is required for employee login.",
      20,
      90,
      { maxWidth: 170 }
    );

    doc.save(`establishment-${created.code}.pdf`);
  };
  if (created) {
    return (
      <AuthLayout title="Establishment Created 🎉">
        <div className="confirmation-card">
          <h2>{created.name}</h2>
          <p><strong>Code:</strong> {created.code}</p>
          <p>{created.address}</p>
          <p>{created.description}</p>

          <div style={{ marginTop: 20 }}>
            <Button onClick={handleDownload}>
              Download information
            </Button>
          </div>

          <div style={{ marginTop: 12 }}>
            <Button onClick={() => navigate("/login")}>
              Go to Login
            </Button>
          </div>
        </div>
      </AuthLayout>
    );
  }
  return (
    <AuthLayout title="Create establishment">

      <ErrorMessage>{error}</ErrorMessage>

      <div className="form-container">

        <Input placeholder="Establishment name" onChange={handleChange("name")} />
        <Input placeholder="Address" onChange={handleChange("address")} />
        <Input placeholder="Description" onChange={handleChange("description")} />

        <h3 className="form-section">Admin</h3>

        <Input placeholder="Admin username" onChange={handleChange("adminUsername")} />
        <div style={{ position: "relative" }}>
        <Input
          type={showPassword ? "text" : "password"}
          placeholder="Admin password"
          onChange={handleChange("adminPassword")}
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
        color: form.adminPassword === confirmPassword ? "green" : "red"
      }}>
        {form.adminPassword === confirmPassword
          ? "Passwords match"
          : "Passwords do not match"}
      </span>
    )}
        <Input placeholder="Admin name" onChange={handleChange("adminName")} />
        <Input placeholder="Admin lastname" onChange={handleChange("adminLastname")} />
        <p className="login__register"> 
            ¿Eres miembro de un establecimiento?{" "}
            <span style={{color: "#11f", cursor: "pointer"}} onClick={() => navigate("/login")}>
              Accede a tu cuenta
            </span>
          </p>
        <Button loading={loading} onClick={handleSubmit}>
          Create establishment
        </Button>

      </div>

    </AuthLayout>
  );
}