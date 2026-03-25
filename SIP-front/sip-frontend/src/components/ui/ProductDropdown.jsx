// ProductDropdown.jsx
import { useState } from "react";
import "../../styles/productDropdown.css";

export default function ProductDropdown({ products, onSelect }) {

  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState(null);

  function handleSelect(product) {
    setSelected(product);
    onSelect(product);
    setOpen(false);
  }

  return (
    <div className="dropdown-container">

      {/* SELECTOR */}
      <div
        className="dropdown-selected"
        onClick={() => setOpen(!open)}
      >
        {selected ? selected.name : "Select product"}
      </div>

      {/* DROPDOWN */}
      {open && (
        <div className="dropdown-list">

          {(products||[]).map((p) => (
            <div
              key={p.id}
              className="product-card-small"
              onClick={() => handleSelect(p)}
            >

              <div className="product-img">🍔</div>

              <div className="product-info">
                <div className="product-name">{p.name}</div>
                <div className="product-price">€{p.price}</div>
              </div>

            </div>
          ))}

        </div>
      )}

    </div>
  );
}