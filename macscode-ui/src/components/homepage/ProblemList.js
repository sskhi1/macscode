import React from 'react';
import { FaCheckCircle, FaRegCircle, FaTimesCircle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import '../../styles/Home.css';

const ProblemList = ({problems, getStatus}) => {
    const navigate = useNavigate();

    const statusIcons = {
        Done: <FaCheckCircle color="green"/>,
        Attempted: <FaTimesCircle color="orange"/>,
        ToDo: <FaRegCircle color="gray"/>
    };

    const handleProblemClick = (problem) => {
        navigate(`/problem/${problem.problemId.course}/${problem.problemId.order}`);
    };

    return (
        <ul className="problem-list">
            {problems.map((problem) => (
                <li key={problem.id} className="problem-item" onClick={() => handleProblemClick(problem)}>
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