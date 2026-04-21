import "../../styles/layout.css";
import img from "../../assets/iconapp.png"

export default function Header({ onMenuClick }) {
  return (
    <header className="app-header">
      <button className="menu-btn" onClick={onMenuClick}>
        ☰
      </button>
      <div className="icon-header">
        <img src={img} alt="SIP logo" className="header-logo" />
      </div>
    </header>
  );
}