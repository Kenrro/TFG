import { useState } from "react";
import Header from "../navigation/Header";
import SideDrawer from "../navigation/SideDrawer";
import "../../styles/layout.css";

export default function AppLayout({ children }) {
  const [open, setOpen] = useState(false);

  return (
    <div className="app-layout">
      <Header onMenuClick={() => setOpen(!open)} />
      <SideDrawer open={open} onClose={() => setOpen(false)} />

      <main className="app-content">
        {children}
      </main>
    </div>
  );
}