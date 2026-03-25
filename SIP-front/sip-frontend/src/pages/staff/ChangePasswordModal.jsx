import { useState } from "react";
import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";
import "../../styles/ChangePasswordModal.css";

export default function ChangePasswordModal({ 
  userId,
  onClose,
  onSubmit
}) {

  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

  const handleSubmit = async () => {
    if (newPassword !== confirmPassword) return;

    try {
      await onSubmit(userId, newPassword);
      setNewPassword("");
      setConfirmPassword("");
      onClose();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="modal-overlay">

      <div className="modal">

        <h3 className="modal-title">Change password</h3>

        {/* NEW PASSWORD */}
        <div className="input-wrapper">
          <Input
            type={showPassword ? "text" : "password"}
            placeholder="New password"
            value={newPassword}
            onChange={(v) => setNewPassword(v)}
          />

          <span
            className="eye"
            onClick={() => setShowPassword(!showPassword)}
          >
            {showPassword ? "🙈" : "👁"}
          </span>
        </div>

        {/* CONFIRM PASSWORD */}
        <div className="input-wrapper">
          <Input
            type={showConfirm ? "text" : "password"}
            placeholder="Confirm password"
            value={confirmPassword}
            onChange={(v) => setConfirmPassword(v)}
          />

          <span
            className="eye"
            onClick={() => setShowConfirm(!showConfirm)}
          >
            {showConfirm ? "🙈" : "👁"}
          </span>
        </div>

        {/* VALIDATION */}
        {confirmPassword && (
          <span
            className={`match ${
              newPassword === confirmPassword ? "ok" : "error"
            }`}
          >
            {newPassword === confirmPassword
              ? "Passwords match"
              : "Passwords do not match"}
          </span>
        )}

        {/* ACTIONS */}
        <div className="modal-actions">
          <Button
            onClick={handleSubmit}
            disabled={!newPassword || newPassword !== confirmPassword}
          >
            Save
          </Button>

          <Button onClick={onClose}>
            Cancel
          </Button>
        </div>

      </div>
    </div>
  );
}