import React from 'react';
import '../styles/Modal.css';

const Modal = ({ message, onConfirm, onCancel }) => {
    return (
        <div className="modal-overlay">
            <div className="modal-container">
                <p className="modal-message">{message}</p>
                <div className="modal-buttons">
                    <button className="modal-confirm-button" onClick={onConfirm}>
                        Yes
                    </button>
                    <button className="modal-cancel-button" onClick={onCancel}>
                        No
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Modal;
