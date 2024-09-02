import React from 'react';
import '../styles/ResultsModal.css';

const resultMessages = {
    PASS: "Accepted",
    FAIL: "Wrong Answer",
    COMPILE_ERROR: "Compile Error"
};

const ResultsModal = ({ show, results, onClose, problemType }) => {
    if (!show) {
        return null;
    }

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h3>Test Case Results</h3>
                {results.map((result, index) => (
                    <div
                        key={index}
                        className={`result-item ${result.result === 'PASS' ? 'pass' : 'fail'}`}
                    >
                        <p><strong>Test Case {result.testNum + 1}:</strong></p>
                        <p><strong>Result:</strong> {resultMessages[result.result] || result.result}</p>
                        {result.additionalInfo && problemType !== "KAREL" &&(
                            <p><strong>Additional Info:</strong> {result.additionalInfo}</p>
                        )}
                    </div>
                ))}
                <button className="close-button" onClick={onClose}>
                    Close
                </button>
            </div>
        </div>
    );
};

export default ResultsModal;
