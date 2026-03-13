import { useState } from "react";
import Header from "../navigation/Header";
import SideDrawer from "../navigation/SideDrawer";
import "../../styles/layout.css";
import { MENU_CONFIG } from "../navigation/menuConfig";
import { decodeToken } from "../../utils/jwt";

export default function AppLayout({ children }) {
  const [open, setOpen] = useState(false);
  const token = decodeToken(localStorage.getItem("token"));
  const menuItems = MENU_CONFIG[token.role];
  return (
    <div className="app-layout">
      <Header onMenuClick={() => setOpen(!open)} />
      <SideDrawer  open={open} onClose={() => setOpen(false)} items={menuItems} />

      <main className="app-content">
        {children}
      </main>
    </div>
  );
}