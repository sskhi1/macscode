import React, { useState, useEffect } from 'react';
import { FaCheckCircle, FaRegCircle, FaTimesCircle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import '../../styles/Home.css';

const ProblemList = ({ problems, getStatus }) => {
    const navigate = useNavigate();
    const [currentPage, setCurrentPage] = useState(1);
    const problemsPerPage = 10;

    const statusIcons = {
        Done: <FaCheckCircle color="green" />,
        Attempted: <FaTimesCircle color="orange" />,
        ToDo: <FaRegCircle color="gray" />
    };

    const handleProblemClick = (problem) => {
        navigate(`/problem/${problem.problemId.course}/${problem.problemId.order}`);
    };

    const indexOfLastProblem = currentPage * problemsPerPage;
    const indexOfFirstProblem = indexOfLastProblem - problemsPerPage;
    const currentProblems = problems.slice(indexOfFirstProblem, indexOfLastProblem);
    const totalPages = Math.ceil(problems.length / problemsPerPage);

    const handlePageChange = (pageNumber) => {
        if (pageNumber >= 1 && pageNumber <= totalPages) {
            setCurrentPage(pageNumber);
        }
    };

    if (problems.length === 0) {
        return <div className="no-message">No problems available</div>;
    }

    return (
        <div>
            <ul className="problem-list">
                {currentProblems.map((problem) => (
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
            <div className="pagination">
                {currentPage > 1 && (
                    <button
                        className="pagination-button"
                        onClick={() => handlePageChange(currentPage - 1)}
                    >
                        &laquo;
                    </button>
                )}
                {Array.from({ length: totalPages }, (_, index) => {
                    if (totalPages > 5) {
                        if (index + 1 === currentPage || index + 1 === 1 || index + 1 === totalPages) {
                            return (
                                <button
                                    key={index + 1}
                                    className={`pagination-button ${currentPage === index + 1 ? 'active' : ''}`}
                                    onClick={() => handlePageChange(index + 1)}
                                >
                                    {index + 1}
                                </button>
                            );
                        } else if (index + 1 === currentPage - 1 || index + 1 === currentPage + 1) {
                            return (
                                <button
                                    key={index + 1}
                                    className="pagination-button"
                                    onClick={() => handlePageChange(index + 1)}
                                >
                                    {index + 1}
                                </button>
                            );
                        } else if (index + 1 === currentPage - 2 || index + 1 === currentPage + 2) {
                            return <span key={index + 1}>...</span>;
                        } else {
                            return null;
                        }
                    } else {
                        return (
                            <button
                                key={index + 1}
                                className={`pagination-button ${currentPage === index + 1 ? 'active' : ''}`}
                                onClick={() => handlePageChange(index + 1)}
                            >
                                {index + 1}
                            </button>
                        );
                    }
                })}
                {currentPage < totalPages && (
                    <button
                        className="pagination-button"
                        onClick={() => handlePageChange(currentPage + 1)}
                    >&raquo;
                    </button>
                )}
            </div>
        </div>
    );
};

export default ProblemList;
