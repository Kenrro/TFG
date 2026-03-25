import { useState, useEffect } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import Button from "../../components/ui/Button";
import BackArrow from "../../components/ui/BackArrow";
import ConfirmModal from "../../components/ui/ConfirmModal";
import CreateIncentiveForm from "../../components/ui/CreateIncentiveForm"

import {
  getIncentives,
  deleteIncentive,
  updateIncentive,
  createIncentive
} from "../../services/incentiveService";
import { useAuth } from "../../contex/AuthContext";
import { decodeToken } from "../../utils/jwt";
import "../../styles/IncentivesView.css"
import { getProducts } from "../../services/productsService";
import { getIncentiveQuantity } from "../../services/transactionService";

export default function IncentivesView() {
  const {token} = useAuth()
  const [incentives, setIncentives] = useState([]);
  const [refresh, setRefresh] = useState(false);
  const [showForm, setShowForm] = useState(false);

  const [deleteId, setDeleteId] = useState(null);
  const [showConfirm, setShowConfirm] = useState(false);

  const [editingId, setEditingId] = useState(null);
  const [editPoints, setEditPoints] = useState("");
  const [editAvailable, setEditAvailable] = useState()
  const [products, setProducts] = useState()
  const [incentivesQuantity, setIncentivesQuantity] = useState([])

  useEffect(() => {
    fetchIncentives();
  }, [refresh]);

  useEffect(() => {
    async function request() {
      try {
        const code = decodeToken(token).establishmentCode
        const response = await getProducts(code)

        const incentiveProductIds = new Set(
          (incentives || []).map(i => i.incentiveResponseDto.productId)
        )

        const productsFiltered = response.filter(product => 
          product.available === true &&
          !(incentiveProductIds).has(product.id)
        )

        setProducts(productsFiltered)

      } catch(err) {
        console.error(err)
      }
    }

    request()


  }, [incentives])
  
  useEffect(() => {
    async function request() {
      try {
        const response = await getIncentiveQuantity();
        console.log(response)
        setIncentivesQuantity(response)
      } catch(err) {
        console.log(err)
      }
    }
    request()
  }, [])

  async function fetchIncentives() {
    try {
      const code = decodeToken(token).establishmentCode;
      const res = await getIncentives(code);
      console.log(res.incentives)
      setIncentives(res.incentives);
    } catch (err) {
      console.error(err);
    }
  }

  async function handleDelete() {
    try {
      await deleteIncentive(deleteId);
      setShowConfirm(false);
      setRefresh(!refresh);
    } catch (err) {
      console.error(err);
    }
  }

  async function handleUpdate(id) {
    try {
      console.log(editAvailable)
      await updateIncentive(id, {
        poinstRequired: Number(editPoints),
        active: editAvailable
      });
      setEditingId(null);
      setRefresh(!refresh);
    } catch (err) {
      console.error(err);
    }
  }
  async function handleCreate(data) {
    try {
      await createIncentive(data)
    } catch(err) {
      console.log(err)
    }
  }

  return (
    <AppLayout>
      <div className="incentives-container">

        <BackArrow />

        <h2 className="title">Incentives</h2>

        {/* GRID */}
        <div className="incentives-grid">

                {incentives.map((item) => {
                  const inc = item.incentiveResponseDto;
                  const product = item.productResponsetDto;
                  const isEditing = editingId === inc.id;

                  return (
                    <div key={inc.id} className="incentive-card">

        <div className="card-header">
          🍔 {product.name}
        </div>

        <div className="card-description">
          {product.description}
        </div>

        <div className="card-points">
          {isEditing ? (
            <input
              value={editPoints}
              onChange={(e) => setEditPoints(e.target.value)}
            />
          ) : (
            <strong>🎁 {inc.pointsRequired} pts</strong>
          )}
        </div>

        {/* 🆕 NUEVO BLOQUE */}
        <div className="card-usage">
          <span className="usage-label">Used:</span>
          <span className="usage-value">
            { 
             incentivesQuantity.find(incentive => Number(incentive.incentiveId) === inc.id)?.quantity
             ?? 0 }
          </span>
        </div>

        <div className={`status ${inc.active ? "active" : "inactive"}`}>
          {isEditing ? (
            <label>
              <input
                type="checkbox"
                checked={editAvailable}
                onChange={(e) => {
                  setEditAvailable(e.target.checked)
                }}
              />
              Available
            </label>
          ) : (
            <strong>{inc.active ? "Active" : "Inactive"}</strong>
          )}
        </div>

        <div className="card-actions">
          {isEditing ? (
            <>
              <button onClick={() => handleUpdate(inc.id)}>💾</button>
              <button onClick={() => setEditingId(null)}>❌</button>
            </>
          ) : (
            <>
              <button onClick={() => {
                setEditingId(inc.id);
                setEditPoints(inc.pointsRequired);
              }}>
                ✏️
              </button>

              <button
                className="delete"
                onClick={() => {
                  setDeleteId(inc.id);
                  setShowConfirm(true);
                }}
              >
                🗑
              </button>
            </>
          )}
        </div>

      </div>
            );
          })}

        </div>

        {/* BUTTON */}
        <div className="actions">
          <Button onClick={() => setShowForm(!showForm)}>
            {showForm ? "Close form" : "Create incentive"}
          </Button>
        </div>

        {/* FORM */}
        {showForm && (
          <div className="form-box">
            <CreateIncentiveForm 
            products={products}
            onCreate={
              async (data) => {
                await createIncentive(data)
                setRefresh(!refresh)
                setShowConfirm(false)
              }
            }/>
          </div>
        )}

      </div>

      {showConfirm && (
        <ConfirmModal
          title="Delete incentive?"
          message="Are you sure?"
          onConfirm={handleDelete}
          onCancel={() => setShowConfirm(false)}
        />
      )}
    </AppLayout>
  );
}