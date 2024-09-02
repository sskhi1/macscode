import React, {useEffect, useState} from 'react';
import '../styles/Problem.css';

const TestCases = ({testCases, onSelect, problemType}) => {
    const [activeTab, setActiveTab] = useState(0);

    useEffect(() => {
        if (onSelect && testCases.length > 0) {
            onSelect(testCases[activeTab]);
        }
    }, [activeTab, testCases, onSelect]);

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
            {problemType !== "KAREL" &&
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
            }
        </div>
    );
};

export default TestCases;
