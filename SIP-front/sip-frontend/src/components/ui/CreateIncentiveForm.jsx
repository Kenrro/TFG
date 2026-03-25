// CreateIncentiveForm.jsx
import { useState } from "react";
import Button from "../../components/ui/Button";
import ProductDropdown from "./ProductDropdown";
import "../../styles/createIncentiveForm.css";

export default function CreateIncentiveForm({ products, onCreate }) {

  const [selectedProduct, setSelectedProduct] = useState(null);
  const [points, setPoints] = useState("");

  function handleSubmit() {
    if (!selectedProduct) return;

    onCreate({
      productId: selectedProduct.id,
      stablishmentCode: selectedProduct.stablishmentCode,
      pointsRequired: Number(points)
    });
  }

  return (
    <div className="form">

      <ProductDropdown
        products={products}
        onSelect={setSelectedProduct}
      />

      <input
        type="number"
        placeholder="Points required"
        value={points}
        onChange={(e) => setPoints(e.target.value)}
      />

      <Button onClick={handleSubmit}>
        Create incentive
      </Button>

    </div>
  );
}