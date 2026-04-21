import { useEffect, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import BackArrow from "../../components/ui/BackArrow";
import "../../styles/CustomersView.css";
import { getCustomers } from "../../services/stablishmentService";
import AdminLayout from "../../components/layouts/AdminLayout";

export default function CustomersView() {

  const [customers, setCustomers] = useState([]);
  const [weeklyCount, setWeeklyCount] = useState(0);

  useEffect(() => {
    async function fetchCustomers() {
      try {
        const res = await getCustomers();

        const relations = res.relations || [];
        console.log(relations)
        setCustomers(relations);

        // 🧠 calcular esta semana
        const now = new Date();
        const weekAgo = new Date();
        weekAgo.setDate(now.getDate() - 7);

        const count = relations.filter(c =>
          new Date(c.registeredAt) >= weekAgo
        ).length;

        setWeeklyCount(count);

      } catch (err) {
        console.error(err);
      }
    }

    fetchCustomers();
  }, []);

  return (
    <>

      <div className="customers-container">

        <BackArrow />

        <h2 className="title">Customers</h2>

        {/* 🔝 RESUMEN */}
        <div className="summary-card">
          👥 Registered this week: <strong>{weeklyCount}</strong>
        </div>

        {/* GRID */}
        <div className="customers-grid">

          {customers.map((rel) => {
            const user = rel.userId;

            return (
              <div key={user.id} className="customer-card">

                {/* IMAGE */}
                <div className="customer-avatar">
                  {user.name.charAt(0).toUpperCase()}
                </div>

                {/* INFO */}
                <div className="customer-info">

                  <div className="customer-name">
                    {user.name} {user.lastname}
                  </div>

                  <div className="customer-username">
                    📱 {user.username}
                  </div>

                  <div className="customer-date">
                    📅 {new Date(rel.registeredAt).toLocaleDateString()}
                  </div>

                  <div className="customer-wallet">
                    💰 {rel.wallet} pts
                  </div>

                </div>

              </div>
            );
          })}

        </div>

      </div>

    </>
  );
}