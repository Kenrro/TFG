import { useEffect, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import { useAuth } from "../../contex/AuthContext";
import { decodeToken } from "../../utils/jwt";
import { getStablishments } from "../../services/stablishmentService";
import "../../styles/staffInfo.css";
import ErrorState from "../../components/ui/ErrorState";
import ChangePasswordModal from "./ChangePasswordModal";
import { changePassword, updateEmployee } from "../../services/authService";
import AdminLayout from "../../components/layouts/AdminLayout";
import ErrorModal from "./ErrorModal";
import BackArrow from "../../components/ui/BackArrow";


export default function AdminInfo() {

  const [error, setError] = useState(null);

  const { token } = useAuth();

  const user = decodeToken(token);

  const [establishment, setEstablishment] = useState(null);
  const [errorConnecting, setErrorConnecting] = useState(false);
  const [loading, setLoading] = useState(true);

  const [isEditing, setIsEditing] = useState(false);

  const [form, setForm] = useState({
    name: user?.name || "",
    lastname: user?.lastname || "",
    username: user?.sub || "",
    role: "ADMIN"
  });

  const [showPasswordModal, setShowPasswordModal] = useState(false);
  useEffect(() => {

    const fetchEstablishment = async () => {

      try {

        const data = await getStablishments();

        setEstablishment(data[0] || null);

      } catch (err) {

        console.error(err);
        setErrorConnecting(true);

      } finally {

        setLoading(false);

      }

    };

    fetchEstablishment();

  }, []);
  async function handleUpdateProfile() {
    try {
      await updateEmployee(user.id, form); // o endpoint tuyo

      setIsEditing(false);
    } catch (err) {
      console.error(err);
    }
  }
  return (
    <>
    <BackArrow></BackArrow>
      <div className="staff-info-container">

        <h2 className="staff-info-title">
          Profile
        </h2>

        <div className="info-card">

        <h3>User information</h3>

        {/* NAME */}
        <p>
          <strong>Name:</strong>{" "}
          {isEditing ? (
            <input
              value={form.name}
              onChange={(e) =>
                setForm({ ...form, name: e.target.value })
              }
            />
          ) : (
            user?.name
          )}
        </p>

        {/* LASTNAME */}
        <p>
          <strong>Lastname:</strong>{" "}
          {isEditing ? (
            <input
              value={form.lastname}
              onChange={(e) =>
                setForm({ ...form, lastname: e.target.value })
              }
            />
          ) : (
            user?.lastname
          )}
        </p>

        {/* USERNAME */}
        <p>
          <strong>Phone:</strong>{" "}
          {isEditing ? (
            <input
              value={form.username}
              onChange={(e) =>
                setForm({ ...form, username: e.target.value })
              }
            />
          ) : (
            user?.sub
          )}
        </p>

        <p><strong>Role:</strong> {user?.role}</p>

        {/* ACTIONS */}
        <div style={{ marginTop: "10px", display: "flex", gap: "10px" }}>

          {isEditing ? (
            <>
              <button onClick={handleUpdateProfile}>💾 Save</button>
              <button onClick={() => setIsEditing(false)}>❌ Cancel</button>
            </>
          ) : (
            <>
              <button onClick={() => setIsEditing(true)}>✏️ Edit</button>
              <button onClick={() => setShowPasswordModal(true)}>🔑 Change password</button>
            </>
          )}

        </div>

      </div>

        
        {loading && <p>Loading establishment...</p>}

        {!loading && errorConnecting && (
          <ErrorState
            message="Error loading establishment information"
            onRetry={() => window.location.reload()}
          />
        )}

        {!loading && establishment && (
          <div className="info-card">

            <h3>Establishment</h3>

            <p><strong>Name:</strong> {establishment.name}</p>
            <p><strong>Code:</strong> {establishment.code}</p>
            <p><strong>Address:</strong> {establishment.address}</p>
            <p><strong>Description:</strong> {establishment.description}</p>

          </div>
        )}

      </div>
        {showPasswordModal && (
          <ChangePasswordModal
            userId={user.id}
            onClose={() => setShowPasswordModal(false)}
            onSubmit={async (id, newPassword) => {
              await changePassword(id, { newPassword });
            }}
          />
        )}
        {error && (
                <ErrorModal
                  message={error}
                  onClose={() => setError(null)}
                />
            )}
    </>
  );
}
