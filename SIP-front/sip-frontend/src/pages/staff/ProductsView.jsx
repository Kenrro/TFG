import { useEffect, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";
import "../../styles/productsView.css";
import { createProduct, deleteProduct, getProducts, updateProduct } from "../../services/productsService";
import { useAuth } from "../../contex/AuthContext";
import { decodeToken } from "../../utils/jwt";
import ConfirmModal from "../../components/ui/ConfirmModal";
import BackArrow from "../../components/ui/BackArrow";

export default function ProductsView() {
  const [productsModified, SetProductsModified] = useState(false)
  const [products, setProducts] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [productToDelete, setProductToDelete] = useState()
  const [showConfirm, setShowConfirm] = useState(false)
  const {token} = useAuth()
  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState({
    name: "",
    description: "",
    price: "",
    available: true
    }); 

  const [form, setForm] = useState({
    name: "",
    description: "",
    price: "",
    available: true
  });

  useEffect(() => {
    async function request() {
        try{
            const code = decodeToken(token).establishmentCode;
            const response = await getProducts(code)
            console.log(response)
            setProducts(response)
        } catch (err) {
            console.error(err)
        }
    }
    request()
  }, [productsModified]);

  const handleCreate = () => {
    async function request() {
        const code = decodeToken(token).establishmentCode;
        try {
            await createProduct({
                ...form,
                code

            })
            SetProductsModified(!productsModified)
        } catch (err) {
            console.error(err)
        }
    }
    request()
  };
  const handleUpdate = (id) => {
    async function request() {
        try {
            await updateProduct(id, editForm)
            SetProductsModified(!productsModified)
        } catch(err) {
            console.error(err)
        }
    }
    request()
  }
  const handleDelete = () => {
      setShowConfirm(false)
    async function request() {
        try {
            await deleteProduct(productToDelete)
            SetProductsModified(!productsModified)
            SetProductsModified(!productsModified)
        } catch(err) {
            console.error(err)
        }
    }
    request()
  }

  return (
    <AppLayout>

      <div className="products-container">
        <BackArrow></BackArrow>
        <h2 className="products-title">
          Products
        </h2>

        {/* GRID */}
        <div className="products-grid">

          {products.map((p) => {

            const isEditing = editingId === p.id;

            return (
                <div key={p.id} className="product-card">

                {/* IMAGE */}
                <div className="product-image">
                    🍔
                </div>

                <div className="product-info">

                    {isEditing ? (
                    <>
                        <input
                        value={editForm.name}
                        onChange={(e) =>
                            setEditForm({ ...editForm, name: e.target.value })
                        }
                        />

                        <input
                        value={editForm.description}
                        onChange={(e) =>
                            setEditForm({ ...editForm, description: e.target.value })
                        }
                        />

                        <input
                        type="number"
                        value={editForm.price}
                        onChange={(e) =>
                            setEditForm({ ...editForm, price: e.target.value })
                        }
                        />

                        <label style={{ fontSize: "12px" }}>
                        <input
                            type="checkbox"
                            checked={editForm.available}
                            onChange={(e) =>
                            setEditForm({ ...editForm, available: e.target.checked })
                            }
                        />
                        Available
                        </label>

                    </>
                    ) : (
                    <>
                        <h3>{p.name}</h3>

                        <p className="product-desc">
                        {p.description}
                        </p>
                    </>
                    )}

                    {/* FOOTER */}
                    <div className="product-footer">

                    {isEditing ? (
                        <span className="price">
                        €{editForm.price}
                        </span>
                    ) : (
                        <span className="price">
                        €{p.price}
                        </span>
                    )}

                    <span className={`status ${p.available ? "available" : "disabled"}`}>
                        {p.available ? "Available" : "Unavailable"}
                    </span>

                    </div>

                    {/* ACTIONS */}
                    <div className="product-actions">

                    {isEditing ? (
                        <>
                        <button
                            onClick={() => {
                            handleUpdate(p.id)
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
                            setEditingId(p.id);
                            setEditForm({
                                name: p.name,
                                description: p.description,
                                price: p.price,
                                available: p.available
                            });
                            }}
                        >
                            ✏️
                        </button>

                        <button
                            style={{ color: "red" }}
                            onClick={() => {
                                setProductToDelete(p.id);
                                setShowConfirm(true)

                            }}
                        >
                            🗑
                        </button>
                        </>
                    )}

                    </div>

                </div>

                </div>
            );
            })}

        </div>

        {/* BUTTON */}
        <div className="products-footer">
          <Button onClick={() => setShowForm(!showForm)}>
            {showForm ? "Close" : "Add product"}
          </Button>
        </div>

        {/* FORM */}
        {showForm && (
          <div className="product-form">

            <Input
              placeholder="Name"
              value={form.name}
              onChange={(v) => setForm({ ...form, name: v })}
            />

            <Input
              placeholder="Description"
              value={form.description}
              onChange={(v) => setForm({ ...form, description: v })}
            />

            <Input
              type="number"
              placeholder="Price"
              value={form.price}
              onChange={(v) => setForm({ ...form, price: v })}
            />

            <Button onClick={handleCreate}>
              Create product
            </Button>

          </div>
        )}

      </div>
        {showConfirm && (
        <ConfirmModal
            title="Delete Product?"
            message="Are you sure you want to delete this product?"
            onConfirm={handleDelete}
            onCancel={() => setShowConfirm(false)}
        />
        )}
    </AppLayout>
  );
}