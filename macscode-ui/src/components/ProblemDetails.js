import React from 'react';
import '../styles/ProblemDetails.css';
import KarelWorld from "./KarelWorld";

function parseKarelWorld(data) {
    const lines = data.trim().split('\n');

    // Read world dimensions
    const width = parseInt(lines[0], 10);
    const height = parseInt(lines[1], 10);

    // Read Karel's position and direction
    const karelX = parseInt(lines[2], 10);
    const karelY = parseInt(lines[3], 10);
    const karelDirection = parseInt(lines[4], 10);

    // Initialize grid
    const grid = [];
    for (let i = 0; i < height; i++) {
        const row = lines[5 + i].split(' ').map(Number);
        grid.push(row);
    }

    // Initialize walls/borders (4 directions: 0 - North, 1 - East, 2 - South, 3 - West)
    const borders = Array.from({ length: height }, () =>
        Array.from({ length: width }, () => [false, false, false, false])
    );

    // Parse the remaining lines for wall positions and add borders accordingly
    for (let i = 5 + height; i < lines.length; i++) {
        const [x, y, direction] = lines[i].split(' ').map(Number);
        borders[height - y - 1][x][direction] = true;
    }

    return {
        width,
        height,
        karelX,
        karelY,
        karelDirection,
        grid,
        borders,
    };
}

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
            {problem.type === "KAREL" && (
                <KarelWorld {...parseKarelWorld(problem.publicTestCases[0].input)} />
            )}
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
