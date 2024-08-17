import React from 'react';
import {FaCheckCircle, FaRegCircle, FaTimesCircle} from 'react-icons/fa';
import '../../styles/Home.css';

const ProblemList = ({problems, getStatus}) => {
    const statusIcons = {
        Done: <FaCheckCircle color="green"/>,
        Attempted: <FaTimesCircle color="orange"/>,
        ToDo: <FaRegCircle color="gray"/>
    };

    return (
        <ul className="problem-list">
            {problems.map((problem) => (
                <li key={problem.id} className="problem-item">
                    <span className="column status status-icon">
                        {statusIcons[getStatus(problem)]}
                    </span>
                    <span className="column title">{problem.problemId.order}. {problem.name}</span>
                    <span className="column type">{problem.type}</span>
                    <span className={`column difficulty ${problem.difficulty.toLowerCase()}`}>
                        {problem.difficulty}
                    </span>
                    <span className="column topics">{problem.topics.join(', ')}</span>
                </li>
            ))}
        </ul>
    );
};

export default ProblemList;