import { useState, useEffect, useContext } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";
import { decodeToken } from "../../utils/jwt";
import { getStaff } from "../../services/stablishmentService";
import { changePassword, createEmployee, deleteEmployee, updateEmployee } from "../../services/authService";
import { useAuth } from "../../contex/AuthContext";
import ConfirmModal from "../../components/ui/ConfirmModal";
import BackArrow from "../../components/ui/BackArrow";
import ChangePasswordModal from "./ChangePasswordModal"; 
import useEmployees from "../../hooks/useEmployees";
import AdminLayout from "../../components/layouts/AdminLayout";
import { useStablishmentContext } from "../../contex/StablishmentContext";
import ErrorModal from "./ErrorModal";

export default function StaffManagement() {
  
  const { token } = useAuth();
  const currentUser = decodeToken(token)
  const { employees } = useStablishmentContext()
  const filteredEmployees = employees.data?.filter(
    (rel) => rel.userId.id !== currentUser.id
  ) || []
  const [showForm, setShowForm] = useState(false);
  
  // Error
  const [error, setError] = useState()
  
  // Confirm modal
  const [showConfirm, setShowConfirm] = useState(false)
  //
  //Password
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  //selected user
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [form, setForm] = useState({
    username: "",
    name: "",
    lastname: "",
    password: ""
  });
  // user to edit
  const [editingId, setEditingId] = useState(null);
  // edit form
  const [editForm, setEditForm] = useState({
    name: "",
    lastname: "",
    username: "",
    role: ""
  });
  // user to delete
  const [deleteUserId, setDeleteUserId] = useState(null);

  // update fetch
  async function handleUpdateEmployee() {
    try {
      await updateEmployee(editingId, editForm);
        employees.refetch()
    } catch (err) {
      console.error(err);
    }
  }
  // To create employees
  async function handleCreateEmployee() {
    try {
      if (form.password !== confirmPassword) {
        alert("Passwords do not match");
        return;
      }

      await createEmployee(form);
      employees.refetch()

      // limpiar form 
      setForm({
        username: "",
        name: "",
        lastname: "",
        password: ""
      });
      setConfirmPassword("");

    } catch (err) {
      console.error(err);
      setError(err.message)
    }
  }
  async function handleDeleteEmployee() {
    try {
      await deleteEmployee(deleteUserId);
      setShowConfirm(false)
      employees.refetch()
    } catch (err) {
      console.error(err);
      setError(err.message)
    }
  }
  async function handleChangePassword(userId, newPassword) {
    try {
      await changePassword  (userId, { newPassword });
    } catch(err) {
      console.error(err)
      setError(err.message)
    }
  }

  const formatDate = (date) => {
    if (!date) return "-";
    return new Date(date).toLocaleDateString();
  };
  
  return (
    <>
      <div style={{ width: "100%", padding: "20px" }}>
        <BackArrow></BackArrow>
        {/* TITLE */}
        <h2 style={{ marginBottom: "20px" }}>
          Employees
        </h2>

        {/* TABLE */}
        <div style={{ overflowX: "auto" }}>
          <table style={{
            width: "100%",
            borderCollapse: "collapse",
            background: "white",
            borderRadius: "10px",
            overflow: "hidden"
          }}>

            <thead style={{ background: "#f5f5f5" }}>
              <tr>
                <th style={th}>Name</th>
                <th style={th}>Last Name</th>
                <th style={th}>Username</th>
                <th style={th}>Role</th>
                <th style={th}>Joined</th>
                <th style={th}>Password</th>
                <th style={th}></th>
              </tr>
            </thead>

            <tbody>
              <tr style={{
                  background: "#f9fafb",
                  borderTop: "1px solid #eee",
                  fontWeight: "500"
                }}>

                  <td style={td}>{currentUser.name}</td>

                  <td style={td}>{currentUser.lastname}</td>

                  <td style={td}>{currentUser.sub}</td>

                  <td style={td}>
                    <span style={{
                      padding: "4px 8px",
                      borderRadius: "6px",
                      fontSize: "12px",
                      background: "#fff4e5",
                      color: "#f59e0b"
                    }}>
                      {currentUser.role}
                    </span>
                  </td>

                  <td style={td}>
                    —
                  </td>
                  <td style={td}>
                    —
                  </td>

                  <td style={{ ...td, color: "#999", fontSize: "12px" }}>
                    Current user
                  </td>

                </tr>
              {filteredEmployees.map((rel) => {
                const user = rel.userId;
                const isEditing = editingId === user.id;
                

                return (
                  <tr key={user.id} style={{ borderTop: "1px solid #eee" }}>

                    {/* NAME */}
                    <td style={td}>
                      {isEditing ? (
                        <input
                          value={editForm.name}
                          onChange={(e) =>
                            setEditForm({ ...editForm, name: e.target.value })
                          }
                        />
                      ) : (
                        `${user.name}`
                      )}
                    </td>
                    <td style={td}>
                      {isEditing ? (
                        <input
                          value={editForm.lastname}
                          onChange={(e) =>
                            setEditForm({ ...editForm, lastname: e.target.value })
                          }
                        />
                      ) : (
                        ` ${user.lastname}`
                      )}
                    </td>

                    {/* USERNAME */}
                    <td style={td}>
                      {isEditing ? (
                        <input
                          value={editForm.username}
                          onChange={(e) =>
                            setEditForm({ ...editForm, username: e.target.value })
                          }
                        />
                      ) : (
                        user.username
                      )}
                    </td>

                    {/* ROLE */}
                    <td style={td}>
                      {isEditing ? (
                        <select
                          value={editForm.role}
                          onChange={(e) =>
                            setEditForm({ ...editForm, role: e.target.value })
                          }
                        >
                          <option value="SELLER">SELLER</option>
                          <option value="ADMIN">ADMIN</option>
                        </select>
                      ) : (
                        <span style={{
                          padding: "4px 8px",
                          borderRadius: "6px",
                          fontSize: "12px",
                          background: user.role === "ADMIN" ? "#e8f0ff" : "#e6f9f0",
                          color: user.role === "ADMIN" ? "#3b82f6" : "#10b981"
                        }}>
                          {user.role}
                        </span>
                      )}
                    </td>

                    {/* DATE */}
                    <td style={td}>
                      {formatDate(rel.registeredAt)}
                    </td>
                    {/* Password */}
                    <td style={td}>
                      <button
                        onClick={() => {
                          setSelectedUserId(user.id);
                          setShowPasswordModal(true);
                        }}
                      >
                        🔑
                      </button>
                    </td>

                    {/* ACTIONS */}
                    <td style={td}>

                      {isEditing ? (
                        <>
                          <button
                            onClick={() => {
                              handleUpdateEmployee();
                              setEditingId(null);
                            }}
                          >
                            💾
                          </button>

                          <button onClick={() => setEditingId(null)}>
                            ❌
                          </button>
                        </>
                      ) : (
                        <>
                          <button
                            onClick={() => {
                              setEditingId(user.id);
                              setEditForm({
                                name: user.name,
                                lastname: user.lastname,
                                username: user.username,
                                role: user.role
                              });
                            }}
                          >
                            ✏️
                          </button>

                          <button onClick={() => {
                            setDeleteUserId(user.id)
                            setShowConfirm(true)}
                            } style={{ color: "red" }}>
                            🗑
                          </button>
                        </>
                      )}

                    </td>

                  </tr>
                );
              })}
            </tbody>

          </table>
        </div>

        {/* BUTTON */}
        <div style={{ marginTop: "20px" }}>
          <Button onClick={() => setShowForm(!showForm)}>
            {showForm ? "Close form" : "Add employee"}
          </Button>
        </div>

        {/* FORM (DESPLEGABLE) */}
        {showForm && (
          <div style={{
            marginTop: "15px",
            padding: "15px",
            background: "white",
            borderRadius: "10px",
            boxShadow: "0 4px 12px rgba(0,0,0,0.08)"
          }}>

            <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>

              <Input
                placeholder="Username"
                value={form.username}
                onChange={(v) => setForm({ ...form, username: v })}
              />

              <Input
                placeholder="Name"
                value={form.name}
                onChange={(v) => setForm({ ...form, name: v })}
              />

              <Input
                placeholder="Lastname"
                value={form.lastname}
                onChange={(v) => setForm({ ...form, lastname: v })}
              />

              <div style={{ position: "relative" }}>
              <Input
                type={showPassword ? "text" : "password"}
                placeholder="Password"
                value={form.password}
                onChange={(v) => setForm({ ...form, password: v })}
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
              value={confirmPassword}
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
              color: form.password === confirmPassword ? "green" : "red"
            }}>
              {form.password === confirmPassword
                ? "Passwords match"
                : "Passwords do not match"}
            </span>
          )}

              <Button onClick={handleCreateEmployee}>
                Create employee
              </Button>

            </div>

          </div>
        )}

      </div>

        {showConfirm && (
                  <ConfirmModal
                    title="Delete user?"
                    message="Are you sure you want to delete this user?"
                    onConfirm={() => handleDeleteEmployee()}
                    onCancel={() => setShowConfirm(false)}
                  />
                )}
        {showPasswordModal && (
          <ChangePasswordModal
            userId={selectedUserId}
            onClose={() => setShowPasswordModal(false)}
            onSubmit={handleChangePassword}
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

const th = {
  textAlign: "left",
  padding: "12px",
  fontSize: "13px",
  color: "#555"
};

const td = {
  padding: "12px",
  fontSize: "14px"
};

const iconBtn = {
  border: "none",
  background: "transparent",
  cursor: "pointer",
  marginRight: "6px"
};