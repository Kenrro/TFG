import { useEffect, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import EstablishmentCard from "../../components/ui/EstablishmentCard";
import EstablishmentSkeleton from "../../components/ui/EstablishmentSkeleton";
import EmptyState from "../../components/ui/EmptyState";
import "../../styles/customer.css";
import { getStablishments } from "../../services/stablishmentService";
import ErrorState from "../../components/ui/ErrorState";
import { useNavigate } from "react-router-dom";

export default function CustomerDashboard() {
  const [establishments, setEstablishments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
   useEffect(() => {
    setTimeout(async () => {
      try {
        const data = await getStablishments();
        setEstablishments(data || []);
      } catch (err) {

        if (err.message === "NETWORK_ERROR") {
          setError("No se pudo conectar con el servidor");
        } else {
          setError("Error cargando establecimientos");
        }

      } finally {
        setLoading(false);
      }
    }, 1200);
  }, []);
    if (error) {
    return (
      <ErrorState
        message={error}
        onRetry={() => window.location.reload()}
      />
    );
  }
  return (
    <AppLayout>
      <h2 className="customer-title">Mis establecimientos</h2>

      {loading && (
        <>
          <EstablishmentSkeleton />
          <EstablishmentSkeleton />
        </>
      )}

      {!loading && error && (
        <p style={{ color: "var(--color-accent)" }}>{error}</p>
      )}

      {!loading && !error && establishments.length === 0 && (
        <EmptyState
          title="Aún no estás inscrito"
          description="Únete a un establecimiento para empezar a acumular puntos."
          actionLabel="Unirme a uno nuevo"
          onAction={()=> navigate("/customer/join-establishment")
          }
        />
      )}

      {!loading && establishments.length > 0 && (
        <>
          {establishments.map((est) => (
            <EstablishmentCard
              key={est.code}
              name={est.name}
              description={est.description}
              address={est.address}
              code={est.code}
              onClick={() => navigate(`/customer/establishment/${est.code}`, {state: est})}
            />
          ))}
          <button className="fab"
          onClick={()=> navigate("/customer/join-establishment")}>➕</button>
        </>
      )}
    </AppLayout>
  );
}