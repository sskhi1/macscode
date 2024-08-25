import React from 'react';
import '../styles/ProblemDetails.css'; // Ensure you have the styles in ProblemDetails.css

const ProblemDetails = ({ problem }) => {
    const difficultyClass = `difficulty ${problem.difficulty.toLowerCase()}`;
    const courseName = problem.problemId.course === "ABS"
        ? "Programming Abstractions"
        : "Programming Methodology";

    return (
        <div className="problem-details">
            <div className="problem-header">
                <h1 className="problem-title">{problem.name}</h1>
                <p className="problem-meta">
                    <strong>Type:</strong> {problem.type} &nbsp;|&nbsp;
                    <strong>Difficulty:</strong> <span className={difficultyClass}>{problem.difficulty}</span>
                </p>
            </div>
            <div className="problem-info">
                <p><strong>Course:</strong> {courseName}</p>
            </div>
            <div className="problem-description">
                <h3>Description</h3>
                <p>{problem.description}</p>
            </div>
            <div className="problem-topics">
                <h3>Topics</h3>
                <ul>
                    {problem.topics.map((topic, index) => (
                        <li key={index}>{topic}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default ProblemDetails;
