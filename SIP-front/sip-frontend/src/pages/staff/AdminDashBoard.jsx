import { useEffect, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import { getDashboard } from "../../services/stablishmentService";
import "../../styles/adminDashboard.css";
import ErrorState from "../../components/ui/ErrorState";
import { useNavigate } from "react-router-dom";
import AdminLayout from "../../components/layouts/AdminLayout";

import {
  Users,
  User,
  Briefcase,
  Package,
  Gift,
  ShoppingCart
} from "lucide-react";
import ErrorModal from "./ErrorModal";

export default function AdminDashboard() {

  // Dashboard data
  const [data, setData] = useState(null);
  const [error, setError] = useState(false);
  // Navigate
  const navigate = useNavigate();
  // fetch dashboard data
  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const res = await getDashboard();
        setData(res);
      } catch (err) {
        console.error(err);
        setError(true);
      }
    };

    fetchDashboard();
  }, []);

  if (error) {
    return (
        <ErrorState 
          message="Error loading dashboard"
          onRetry={() => window.location.reload()}
        />
    );
  }

  if (!data) {
    return (
        <p>Loading dashboard...</p>
    );
  }

  return (
    <>
      <div className="dashboard-container">

        <h2 className="dashboard-title">Dashboard</h2>

        {/* USERS GROUP */}
        <div className="group">
          <h3 className="group-title">Users</h3>

          <div className="group-grid">

            <div className="card users" onClick={() => navigate("/admin/customers")}>
              <div className="card-icon">
                <User size={22} />
              </div>
              <div>
                <p className="big">{data.usersQuantity.customers}</p>
                <span>Customers</span>
              </div>
            </div>

            <div className="card users" onClick={() => navigate("/admin/management")}>
              <div className="card-icon">
                <Briefcase size={22} />
              </div>
              <div>
                <p className="big">{data.usersQuantity.employees}</p>
                <span>Employees</span>
              </div>
            </div>

          </div>
        </div>

        {/* TRANSACTIONS SOLO (destacado) */}
        <div className="card transactions full" onClick={()=> navigate("/admin/transactions")}>
          <div className="card-icon">
            <ShoppingCart size={26} />
          </div>
          <div>
            <h3>Transactions</h3>
            <p className="big">
              {data.transactionInformation.transactionsQuantity} transactions
            </p>
            <span>
              {data.transactionInformation.pointsAwarded} pts awarded
            </span>
          </div>
        </div>

        {/* BUSINESS GROUP */}
        <div className="group">
          <h3 className="group-title">Business</h3>

          <div className="group-grid">

            <div className="card products" onClick={() => navigate("/admin/products")}>
              <div className="card-icon">
                <Package size={22} />
              </div>
              <div>
                <p className="big">{data.productsQuantity}</p>
                <span>Products</span>
              </div>
            </div>

            <div className="card incentives" onClick={() => navigate("/admin/incentives")}>
              <div className="card-icon">
                <Gift size={22} />
              </div>
              <div>
                <p className="big">{data.incentiveQuantity}</p>
                <span>Incentives</span>
              </div>
            </div>

          </div>
        </div>

      </div>
      {error && (
        <ErrorModal
          message={error}
          onClose={() => setError(null)}
        />
)}
    </>
  );
}