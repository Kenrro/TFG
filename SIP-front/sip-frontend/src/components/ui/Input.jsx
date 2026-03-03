import "../../styles/ui.css"

export default function Input({ onChange, ...props }) {
  const handleChange = (e) => {
    onChange?.(e.target.value);
  };

  return (
    <input className="ui-input" {...props} onChange={handleChange} />
  );
}