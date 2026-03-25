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
import StaffInfo from "./pages/staff/StaffInfo"
import StaffGivePointsView from "./pages/staff/StaffGivePointsView"
import StaffScanView from "./pages/staff/StaffScanView"
import AdminDashboard from "./pages/staff/AdminDashBoard"
import StaffManagement from "./pages/staff/StaffManagement"
import TransactionsView from "./pages/staff/TransactionsView"
import ProductsView from "./pages/staff/ProductsView"
import IncentivesView from "./pages/staff/IncentivesView.jsx"
import SettingsView from "./pages/staff/SettingsView.jsx"
import CustomersView from "./pages/staff/CustomersView.jsx"
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
      <Route
        path="/admin"
        element={
          <ProtectedRoute roles={["ADMIN"]}>
            <AdminDashboard />
          </ProtectedRoute>
        }
      />
      <Route
        path="/staff/info"
        element={
          <ProtectedRoute roles={["SELLER", "ADMIN"]}>
            <StaffInfo />
          </ProtectedRoute>
        }
      />
      <Route
        path="/staff/give-points"
        element={
          <ProtectedRoute roles={["SELLER", "ADMIN"]}>
            <StaffGivePointsView />
          </ProtectedRoute>
        }
      />
      <Route
        path="/staff/redeem"
        element={
          <ProtectedRoute roles={["SELLER", "ADMIN"]}>
            <StaffScanView />
          </ProtectedRoute>
        }
      />
      <Route
        path="/staff/management"
        element={
          <ProtectedRoute roles={["ADMIN"]}>
            <StaffManagement />
          </ProtectedRoute>
        }
      />
      <Route
        path="/admin/transactions"
        element={
          <ProtectedRoute roles={["ADMIN"]}>
            <TransactionsView />
          </ProtectedRoute>
        }
      />
      <Route
        path="/admin/products"
        element={
          <ProtectedRoute roles={["ADMIN"]}>
            <ProductsView />
          </ProtectedRoute>
        }
      />
      <Route
        path="/admin/incentives"
        element={
          <ProtectedRoute roles={["ADMIN"]}>
            <IncentivesView />
          </ProtectedRoute>
        }
      />
      <Route
        path="/admin/settings"
        element={
          <ProtectedRoute roles={["ADMIN"]}>
            <SettingsView />
          </ProtectedRoute>
        }
      />
      <Route
        path="/admin/customers"
        element={
          <ProtectedRoute roles={["ADMIN"]}>
            <CustomersView />
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
