import { useEffect, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import { useAuth } from "../../contex/AuthContext";
import { decodeToken } from "../../utils/jwt";
import { getStablishments } from "../../services/stablishmentService";
import "../../styles/staffInfo.css";
import ErrorState from "../../components/ui/ErrorState";
import BackArrow from "../../components/ui/BackArrow";


export default function StaffInfo() {

  const { token } = useAuth();

  const user = decodeToken(token);

  const [establishment, setEstablishment] = useState(null);
  const [errorConnecting, setErrorConnecting] = useState(false);
  const [loading, setLoading] = useState(true);
  useEffect(() => {

    const fetchEstablishment = async () => {

      try {

        const data = await getStablishments();

        setEstablishment(data[0] || null);

      } catch (err) {

        console.error(err);
        setErrorConnecting(true);

      } finally {

        setLoading(false);

      }

    };

    fetchEstablishment();

  }, []);

  return (
    <AppLayout>
      <BackArrow></BackArrow>
      <div className="staff-info-container">

        <h2 className="staff-info-title">
          Profile
        </h2>

        <div className="info-card">

          <h3>User information</h3>

          <p><strong>Name:</strong> {user?.name}</p>
          <p><strong>Lastname:</strong> {user?.lastname}</p>
          <p><strong>Phone:</strong> {user?.sub}</p>
          <p><strong>Role:</strong> {user?.role}</p>

        </div>

        
        {loading && <p>Loading establishment...</p>}

        {!loading && errorConnecting && (
          <ErrorState
            message="Error loading establishment information"
            onRetry={() => window.location.reload()}
          />
        )}

        {!loading && establishment && (
          <div className="info-card">

            <h3>Establishment</h3>

            <p><strong>Name:</strong> {establishment.name}</p>
            <p><strong>Code:</strong> {establishment.code}</p>
            <p><strong>Address:</strong> {establishment.address}</p>
            <p><strong>Description:</strong> {establishment.description}</p>

          </div>
        )}

      </div>

    </AppLayout>
  );
}  