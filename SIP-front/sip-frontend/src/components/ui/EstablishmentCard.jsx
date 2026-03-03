import PlaceholderImage from "./PlaceholderImage";
import "../../styles/customer.css";

export default function EstablishmentCard({ name, description, address, code, onClick }) {
  return (
    <div className="est-card" onClick={onClick}>
      
      <PlaceholderImage />

      <div className="est-card-content">
        <h3>{name}</h3>
        <p className="est-card-address">{address}</p>
        <p className="est-card-description">{description}</p>

        <span className="est-card-code">
          Código: {code}
        </span>
      </div>

    </div>
  );
}