import { useEffect, useRef, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import "../../styles/transactionsView.css";
import { getGivePointsTransactions, getRedeemTransactions } from "../../services/transactionService";
import BackArrow from "../../components/ui/BackArrow";
import Draggable from "react-draggable";

import AdminLayout from "../../components/layouts/AdminLayout";
import { useStablishmentContext } from "../../contex/StablishmentContext";

export default function TransactionsView() {

  const nodeRef = useRef(null)
  // Transactions
  const [redeemTx, setRedeemTx] = useState([]);
  const [pointsTx, setPointsTx] = useState([]);
  // user card
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const { employees } = useStablishmentContext();
  // Get give points transactions
  useEffect(() => {
    async function request() {
        try{
            const res = await getGivePointsTransactions();
            console.log(res)
            setPointsTx(res.transactions);
        } catch (ex) {
            console.error(ex);
        }
    }
    request()
  }, []);
  // Get redeem transactions
  useEffect(() => {
    async function request() {
        try{
            const res = await getRedeemTransactions();
            console.log(res)
            setRedeemTx(res.transactions);
        } catch (ex) {
            console.error(ex);
        }
    }
    request()
  }, []);
  // format date
  const formatDate = (date) => {
    if (!date) return "-";
    return new Date(date).toLocaleString();
  };
  // Status type`s
  const getStatusClass = (status) => {
    switch (status) {
      case "COMPLETED":
        return "status completed";
      case "FAILED":
        return "status failed";
      case "PENDING":
        return "status pending";
      case "PROCESSING":
        return "status processing";
      case "EXPIRED":
        return "status expired";
      default:
        return "status";
    }
  };

  return (
    <>
      <BackArrow></BackArrow>
      <div className="transactions-container">

        <h2 className="transactions-title">
          Transactions
        </h2>

        {/* REDEEM */}
        <h3 className="transactions-subtitle">
          Redeem Transactions
        </h3>

        <div className="table-container">
          <table className="table">

            <thead>
              <tr>
                <th>Product</th>
                <th>Incentive</th>
                <th>Customer</th>
                <th>Employee</th>
                <th>Points</th>
                <th>Status</th>
                <th>Created</th>
                <th>Expires</th>
              </tr>
            </thead>

            <tbody>
              {redeemTx.map((tx) => (
                <tr key={tx.id}>
                  <td>{tx.productId}</td>
                  <td>{tx.incentiveId}</td>
                  <td>{tx.customerId || "-"}</td>
                  <td>
                    {tx.employeeId ? (
                      <span
                        style={{ cursor: "pointer", color: "#3b82f6" }}
                        onClick={() => {
                          const emp = employees.data?.find(
                            (e) => e.userId.id === tx.employeeId
                          );
                          setSelectedEmployee(emp);
                        }}
                      >
                        {tx.employeeId}
                      </span>
                    ) : "-"}
                  </td>               
                  <td>{tx.pointsRequired}</td>                  
                  <td>{tx.status}</td>                  
                  <td>{formatDate(tx.createdAt)}</td>
                  <td>{formatDate(tx.expiresAt)}</td>
                </tr>
              ))}
            </tbody>

          </table>
        </div>

        {/* POINTS */}
        <h3 className="transactions-subtitle">
          Points Transactions
        </h3>

        <div className="table-container">
          <table className="table">

            <thead>
              <tr>
                <th>Amount (€)</th>
                <th>Points</th>
                <th>Customer</th>
                <th>Employee</th>
                <th>Status</th>
                <th>Created</th>
                <th>Expires</th>
              </tr>
            </thead>

            <tbody>
              {pointsTx.map((tx) => (
                <tr key={tx.id}>

                  <td>{tx.amountSpent}</td>

                  <td>{tx.pointsGiven}</td>

                  <td>{tx.customerId || "-"}</td>
                  <td>
                    {tx.employeeId ? (
                      <span
                        style={{ cursor: "pointer", color: "#3b82f6" }}
                        onClick={() => {
                          const emp = employees.data?.find(
                            (e) => e.userId.id === tx.employeeId
                          );
                          setSelectedEmployee(emp);
                        }}
                      >
                        {tx.employeeId}
                      </span>
                    ) : "-"}
                  </td>   

                  <td>
                    <span className={getStatusClass(tx.status)}>
                      {tx.status}
                    </span>
                  </td>

                  <td>{formatDate(tx.createdAt)}</td>
                  <td>{formatDate(tx.expiresAt)}</td>

                </tr>
              ))}
            </tbody>

          </table>
        </div>

      </div>
      {selectedEmployee && (
        <Draggable nodeRef={nodeRef}>

          <div className="employee-card" ref={nodeRef}>
            <h4>
              {selectedEmployee.userId.name} {selectedEmployee.userId.lastname}
            </h4>

            <p><strong>Username:</strong> {selectedEmployee.userId.username}</p>
            <p><strong>Role:</strong> {selectedEmployee.userId.role}</p>
            <p><strong>Joined:</strong> {formatDate(selectedEmployee.registeredAt)}</p>

            <button onClick={() => setSelectedEmployee(null)}>
              Close
            </button>
          </div>
        </Draggable>
      )}

    </>
  );
}