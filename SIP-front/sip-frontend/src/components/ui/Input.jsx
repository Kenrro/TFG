import "../../styles/ui.css";

export default function Input({ value, onChange, error, ...props }) {

  const handleChange = (e) => {
    if (onChange) {
      onChange(e.target.value);
    }
  };

  return (
    <div className="ui-input-wrapper">

      <input
        className={`ui-input ${error ? "ui-input-error" : ""}`}
        value={value}
        onChange={handleChange}
        {...props}
      />

      {error && (
        <span className="ui-input-error-text">
          {error}
        </span>
      )}

    </div>
  );
}