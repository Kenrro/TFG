import { useState } from "react";
import Header from "../navigation/Header";
import SideDrawer from "../navigation/SideDrawer";
import "../../styles/layout.css";
import { MENU_CONFIG } from "../navigation/menuConfig";
import { decodeToken } from "../../utils/jwt";
import { StablishmentProvider } from "../../contex/StablishmentContext";

import { Outlet } from "react-router-dom";

export default function AdminLayout() {
  const [open, setOpen] = useState(false);
  const token = decodeToken(localStorage.getItem("token"));
  const menuItems = MENU_CONFIG[token.role];

  return (
    <StablishmentProvider>
      <div className="app-layout">
        <Header onMenuClick={() => setOpen(!open)} />
        <SideDrawer open={open} onClose={() => setOpen(false)} items={menuItems} />

        <main className="app-content">
          <Outlet />
        </main>
      </div>
    </StablishmentProvider>
  );
}