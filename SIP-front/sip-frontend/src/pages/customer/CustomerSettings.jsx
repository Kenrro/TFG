import { useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";
import { useAuth } from "../../contex/AuthContext";
import { decodeToken } from "../../utils/jwt";
import "../../styles/settings.css";
import { deleteCustomer, updateCustomer } from "../../services/authService";
import ConfirmModal from "../../components/ui/ConfirmModal";

export default function CustomerSettings() {

  const { token, logout } = useAuth();
  const user = decodeToken(token);
  const [showConfirm, setShowConfirm] = useState(false);
  const [form, setForm] = useState({
    name: user?.name || "",
    lastname: user?.lastname || "",
    password: "",
    confirmPassword: ""
    
  });

  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (field) => (value) => {
    setForm(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleUpdate = async () => {

    setError(null);

    if (form.password && form.password !== form.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {

      const payload = {
        username: user.sub,
        name: form.name,
        lastname: form.lastname,
        ...(form.password && { password: form.password }),
        role: user.role
      };
      
      console.log("update user", payload);

      await updateCustomer(payload);

      alert("Profile updated");

    } catch (err) {

      console.error(err);
      setError("Error updating profile");

    }

  };

  const handleDeleteAccount = async () => {

  
    try {

        await deleteCustomer();

      logout();

    } catch (err) {

      console.error(err);

    }

  };

  return (
    <AppLayout>

      <div className="settings-container">

        <h2 className="settings-title">
          Account Settings
        </h2>

        <div className="settings-card">

          <h3>Account info</h3>

          <p><strong>Phone:</strong> {user?.sub}</p>
          <p><strong>Role:</strong> {user?.role}</p>

        </div>

        <div className="settings-card">

          <h3>Edit profile</h3>

          <Input
            placeholder="Name"
            value={form.name}
            onChange={handleChange("name")}
          />

          <Input
            placeholder="Lastname"
            value={form.lastname}
            onChange={handleChange("lastname")}
          />

          <Input
            type={showPassword ? "text" : "password"}
            placeholder="New password"
            value={form.password}
            onChange={handleChange("password")}
          />

          <Input
            type={showPassword ? "text" : "password"}
            placeholder="Confirm password"
            value={form.confirmPassword}
            onChange={handleChange("confirmPassword")}
          />

          <label className="show-password">
            <input
              type="checkbox"
              onChange={() => setShowPassword(!showPassword)}
            />
            Show password
          </label>

          {error && (
            <p className="settings-error">{error}</p>
          )}

          <Button onClick={handleUpdate}>
            Update profile
          </Button>

        </div>

        <div className="settings-card danger">

          <h3>Danger zone</h3>

          <Button
            className="delete-btn"
            onClick={() => setShowConfirm(true)}
          >
            Delete account
          </Button>

        </div>

      </div>
                  {showConfirm && (
                    <ConfirmModal
                      title="Delete account?"
                      message="Are you sure you want to delete your account? This action cannot be undone."
                      onConfirm={handleDeleteAccount}
                      onCancel={() => setShowConfirm(false)}
                    />
                  )}
    </AppLayout>
  );
}