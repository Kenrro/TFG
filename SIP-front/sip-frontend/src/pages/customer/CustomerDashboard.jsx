import { useEffect, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import EstablishmentCard from "../../components/ui/EstablishmentCard";
import EstablishmentSkeleton from "../../components/ui/EstablishmentSkeleton";
import EmptyState from "../../components/ui/EmptyState";
import "../../styles/customer.css";

export default function CustomerDashboard() {
  const [establishments, setEstablishments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Simulación fetch backend
    setTimeout(() => {
      try {
        setEstablishments([]); // prueba vacío
        setLoading(false);
      } catch (e) {
        setError("Error al cargar establecimientos");
        setLoading(false);
      }
    }, 1200);
  }, []);

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
          onAction={() => console.log("Ir a unir establecimiento")}
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
            />
          ))}
          <button className="fab">➕</button>
        </>
      )}
    </AppLayout>
  );
}