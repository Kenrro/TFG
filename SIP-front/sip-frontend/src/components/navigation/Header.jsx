import "../../styles/layout.css";

export default function Header({ onMenuClick }) {
  return (
    <header className="app-header">
      <button className="menu-btn" onClick={onMenuClick}>
        ☰
      </button>
      <h1 className="app-title">Loyalty App</h1>
    </header>
  );
}