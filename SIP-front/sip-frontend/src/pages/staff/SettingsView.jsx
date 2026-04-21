import { useEffect, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";
import "../../styles/SettingsView.css";
import { getStablishments, updateStablishment, deleteStablishment } from "../../services/stablishmentService";
import { getConfiguration, updateConfiguration } from "../../services/configurationService"
import ConfirmModal from "../../components/ui/ConfirmModal"
import { useAuth } from "../../contex/AuthContext";
import AdminLayout from "../../components/layouts/AdminLayout";
import { useStablishmentContext } from "../../contex/StablishmentContext";
import ErrorModal from "./ErrorModal";
import BackArrow from "../../components/ui/BackArrow";


export default function SettingsView() {

  const [error, setError] = useState(null)

  const [establishment, setEstablishment] = useState({
    name: "",
    description: "",
    address: ""
  });
  const {stablishment, configuration} = useStablishmentContext()
  const { logout } = useAuth();
  const handleLogout = () => {
      logout();
      navigate("/login");
    };
  const [points, setPoints] = useState(0);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

  // 🔄 LOAD DATA
  useEffect(() => {
    if (stablishment.data && configuration.data) {
      setEstablishment({
        name: stablishment.data.name,
        description: stablishment.data.description,
        address: stablishment.data.address
      });

      setPoints(configuration.data.points_per_euro);
    }
  }, [stablishment.data, configuration.data]);

  // 💾 SAVE ESTABLISHMENT
  const handleSaveEstablishment = async () => {
    try {
      await updateStablishment(establishment);
      stablishment.refetch()
    } catch (e) {
      console.error(e);
      setError(e.message)
    }
  };

  // 💾 SAVE CONFIG
  const handleSaveConfig = async () => {
    try {
      await updateConfiguration({
        point_per_euro: Number(points)
      });
      await configuration.refetch()
    } catch (e) {
      console.error(e);
      setError(e.message)
    }
  };
  const handleDeleteEstablishment = async () => {
  try {
    await deleteStablishment();
    setShowDeleteConfirm(false);
    handleLogout()
  } catch (e) {
    console.error(e);
    setError(e.message)
  }
};
  if(stablishment.stablishmentLoading) {
    return(
      <div>Loading stablishment...</div>
    )
  }
  return (
    <>
    <BackArrow></BackArrow>
      <div className="settings-container">

        <h2 className="settings-title">
          ⚙️ Settings
        </h2>

        {/* ESTABLISHMENT */}
        <div className="settings-card">

          <h3>🏪 Business information</h3>

          <Input
            placeholder="Name"
            value={establishment.name}
            onChange={(v) =>
              setEstablishment({ ...establishment, name: v })
            }
          />

          <Input
            placeholder="Description"
            value={establishment.description}
            onChange={(v) =>
              setEstablishment({ ...establishment, description: v })
            }
          />

          <Input
            placeholder="Address"
            value={establishment.address}
            onChange={(v) =>
              setEstablishment({ ...establishment, address: v })
            }
          />

          <Button onClick={handleSaveEstablishment}>
            Save changes
          </Button>

        </div>

        {/* CONFIG */}
        <div className="settings-card">

          <h3>💰 Points system</h3>

          <div className="points-row">
            <span>Points per €</span>

            <input
              type="number"
              value={points}
              onChange={(e) => setPoints(e.target.value)}
            />
          </div>

          <Button onClick={handleSaveConfig}>
            Update points
          </Button>

        </div>
            <Button onClick={() => setShowDeleteConfirm(true)}>
              Delete establishment
            </Button>
      </div>
      {showDeleteConfirm && (
    <ConfirmModal
          title="Delete establishment?"
          message="This action cannot be undone."
          onConfirm={handleDeleteEstablishment}
          onCancel={() => setShowDeleteConfirm(false)}
        />
      )}

      {error && (
              <ErrorModal
                message={error}
                onClose={() => setError(null)}
              />
      )}
    </>
    
  );
}