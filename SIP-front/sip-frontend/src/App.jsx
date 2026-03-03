import { Navigate, Route, Routes } from "react-router-dom"
import LoginView from "./pages/customer/LoginView"
import RegisterView from "./pages/customer/RegisterCustomer"
import CreateEstablishmentView from "./pages/CreateStablishmentView"
import CustomerDashboardoard from "./pages/customer/CustomerDashboard"
import StaffDashboard from "./pages/staff/StaffDashBoard"
import ProtectedRoute from "./routes/ProtectedRoute"
import PublicRoute from "./routes/PublicRoute"
import CustomerSettings from "./pages/customer/CustomerSettings"
function App() {

  return (
    <Routes>
      {/* PUBLIC */}
      <Route
        path="/login"
        element={
          <PublicRoute>
            <LoginView />
          </PublicRoute>
        }
      />
      <Route
        path="/register"
        element={
          <PublicRoute>
            <RegisterView />
          </PublicRoute>
        }
      />
      {/* Create stablishment */}
      <Route path="/create-establishment" element={<CreateEstablishmentView />} />

      {/* CUSTOMER */}
      <Route
        path="/customer"
        element={
          <ProtectedRoute roles={["CUSTOMER"]}>
            <CustomerDashboardoard />
          </ProtectedRoute>
        }
      />
      <Route
        path="/customer/settings"
        element={
          <ProtectedRoute roles={["CUSTOMER"]}>
            <CustomerSettings />
          </ProtectedRoute>
        }
      />

      {/* STAFF (SELLER + ADMIN) */}
      <Route
        path="/staff"
        element={
          <ProtectedRoute roles={["SELLER", "ADMIN"]}>
            <StaffDashboard />
          </ProtectedRoute>
        }
      />
      {/* TEMP redirect root */}
      <Route path="/" element={<Navigate to="/login" replace />} />
      {/* fallback */}
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  )
}

export default App
