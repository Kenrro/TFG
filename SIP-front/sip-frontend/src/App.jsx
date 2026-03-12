import { Navigate, Route, Routes } from "react-router-dom"
import LoginView from "./pages/customer/LoginView"
import RegisterView from "./pages/customer/RegisterCustomer"
import CreateEstablishmentView from "./pages/CreateStablishmentView"
import CustomerDashboardoard from "./pages/customer/CustomerDashboard"
import StaffDashboard from "./pages/staff/StaffDashBoard"
import ProtectedRoute from "./routes/ProtectedRoute"
import PublicRoute from "./routes/PublicRoute"
import CustomerSettings from "./pages/customer/CustomerSettings"
import CustomerEstablishmentView from "./pages/customer/CustomerEstablishmentView"
import CustomerScanView from "./pages/customer/CustomerScanView"
import CustomerRedeemView from "./pages/customer/CustomerRedeemView"
import JoinEstablishmentView from "./pages/customer/JoinStablishmentView"
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
      <Route
        path="/customer/join-establishment"
        element={
          <ProtectedRoute roles={["CUSTOMER"]}>
            <JoinEstablishmentView />
          </ProtectedRoute>
        }
      />
      <Route
        path="/customer/establishment/:code"
        element={
          <ProtectedRoute roles={["CUSTOMER"]}>
            <CustomerEstablishmentView />
          </ProtectedRoute>
        }
      />
      <Route
        path="/customer/scan"
        element={
          <ProtectedRoute roles={["CUSTOMER"]}>
            <CustomerScanView />
          </ProtectedRoute>
        }
      />
      <Route
        path="/customer/redeem"
        element={
          <ProtectedRoute roles={["CUSTOMER"]}>
            <CustomerRedeemView />
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
