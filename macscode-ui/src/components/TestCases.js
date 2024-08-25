import React, { useState } from 'react';
import '../styles/Problem.css';

const TestCases = ({ testCases }) => {
    const [activeTab, setActiveTab] = useState(0);

    return (
        <div className="test-cases">
            <div className="test-cases-tabs">
                {testCases.map((_, index) => (
                    <button
                        key={index}
                        className={`tab-button ${index === activeTab ? 'active' : ''}`}
                        onClick={() => setActiveTab(index)}
                    >
                        Test Case {index + 1}
                    </button>
                ))}
            </div>
            <div className="test-case-content">
                <h4>Test Case {activeTab + 1}</h4>
                <div>
                    <h5>Input</h5>
                    <pre>{testCases[activeTab].input}</pre>
                </div>
                <div>
                    <h5>Expected Output</h5>
                    <pre>{testCases[activeTab].expectedOutput}</pre>
                </div>
            </div>
        </div>
    );
};

export default TestCases;
