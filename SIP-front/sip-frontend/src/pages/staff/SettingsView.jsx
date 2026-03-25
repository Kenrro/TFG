import { useEffect, useState } from "react";
import AppLayout from "../../components/layouts/AppLayout";
import Input from "../../components/ui/Input";
import Button from "../../components/ui/Button";
import "../../styles/settingsView.css";
import { getStablishments, updateStablishment, deleteStablishment } from "../../services/stablishmentService";
import { getConfiguration, updateConfiguration } from "../../services/configurationService"
import ConfirmModal from "../../components/ui/ConfirmModal"
import { useAuth } from "../../contex/AuthContext";

export default function SettingsView() {

  const [establishment, setEstablishment] = useState({
    name: "",
    description: "",
    address: ""
  });
  const { logout } = useAuth();
  const handleLogout = () => {
      logout();
      navigate("/login");
    };
  const [points, setPoints] = useState(0);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

  // 🔄 LOAD DATA
  useEffect(() => {
    async function load() {
      try {
        // TODO: tus services
        const [est] = await getStablishments();
        const conf = await getConfiguration();
        console.log(conf)
        setEstablishment({
          name: est.name,
          description: est.description,
          address: est.address
        });

        setPoints(conf.points_per_euro);

      } catch (e) {
        console.error(e);
      }
    }

    load();
  }, []);

  // 💾 SAVE ESTABLISHMENT
  const handleSaveEstablishment = async () => {
    try {
      await updateStablishment(establishment);
    } catch (e) {
      console.error(e);
    }
  };

  // 💾 SAVE CONFIG
  const handleSaveConfig = async () => {
    try {
      await updateConfiguration({
        point_per_euro: Number(points)
      });
    } catch (e) {
      console.error(e);
    }
  };
  const handleDeleteEstablishment = async () => {
  try {
    await deleteStablishment();
    setShowDeleteConfirm(false);
    handleLogout()
  } catch (e) {
    console.error(e);
  }
};

  return (
    <AppLayout>
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
    </AppLayout>
    
  );
}