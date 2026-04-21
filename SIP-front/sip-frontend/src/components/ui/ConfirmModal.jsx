import "../../styles/ui.css"
export default function ConfirmModal({ title, message, onConfirm, onCancel }) {
    return (
        <div className="confirm-overlay">

            <div className="confirm-modal">

            <h3>{title}</h3>

            <p>
                {message}
            </p>

            <div className="confirm-actions">

                <button
                className="cancel-btn"
                onClick={() => onCancel(false)}
                >
                Cancel
                </button>

                <button
                className="confirm-btn"
                onClick={onConfirm}
                >
                Confirm
                </button>

            </div>

            </div>

        </div>
    )
}