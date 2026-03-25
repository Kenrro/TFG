import { useNavigate } from "react-router-dom";
import { FaArrowLeft } from "react-icons/fa";

export default function BackArrow() {
  const navigate = useNavigate();

  return (
    <FaArrowLeft
      onClick={() => navigate(-1)}
      style={{ cursor: "pointer", fontSize: "20px" }}
    />
  );
}