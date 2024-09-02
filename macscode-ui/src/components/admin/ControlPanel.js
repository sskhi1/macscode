import React, { useState, useEffect, useCallback, useContext } from 'react';
import axios from 'axios';
import '../../styles/ControlPanel.css';
import TopBar from '../TopBar';
import { AuthContext } from "../../AuthContext";
import { useNavigate } from "react-router-dom";
import Modal from '../Modal';

const ControlPanel = () => {
    const { auth } = useContext(AuthContext);
    const [activeTab, setActiveTab] = useState('manage-users');
    const [users, setUsers] = useState([]);
    const [filteredUsers, setFilteredUsers] = useState([]);
    const [problems, setProblems] = useState([]);
    const [filteredProblems, setFilteredProblems] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [usersPerPage, setUsersPerPage] = useState(10);
    const problemsPerPage = 10;
    const [confirmAction, setConfirmAction] = useState(null);
    const [confirmData, setConfirmData] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();

    const fetchUsers = useCallback(async () => {
        try {
            const response = await axios.get('/auth/users/all', {
                headers: {
                    Authorization: `Bearer ${auth}`
                }
            });
            setUsers(response.data);
            setFilteredUsers(response.data);
        } catch (err) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    }, [auth]);

    const fetchProblems = useCallback(async () => {
        try {
            const response = await axios.get('/problems-service/problems/all', {
                headers: {
                    Authorization: `Bearer ${auth}`
                }
            });
            let data = response.data;
            setProblems(data);
            setFilteredProblems(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    }, [auth]);

    useEffect(() => {
        if (activeTab === 'manage-problems') {
            fetchProblems();
        } else if (activeTab === 'manage-users') {
            fetchUsers();
        }
    }, [activeTab, fetchProblems]);

    useEffect(() => {
        if (activeTab === 'manage-users') {
            const filtered = users.filter(user => user.username.toLowerCase().includes(searchTerm.toLowerCase()));
            setFilteredUsers(filtered.sort((a, b) => a.username.localeCompare(b.username)));
            setCurrentPage(1);
        } else if (activeTab === 'manage-problems') {
            const filtered = problems.filter(problem => problem.name.toLowerCase().includes(searchTerm.toLowerCase()));
            setFilteredProblems(filtered.sort((a, b) => a.name.localeCompare(b.name)));
            setCurrentPage(1);
        }
    }, [searchTerm, activeTab, users, problems]);

    const handleProblemClick = (problem) => {
        navigate(`/problem/${problem.problemId.course}/${problem.problemId.order}`);
    };

    const handleDeleteProblem = async (problemId) => {
        try {
            await axios.delete(`/problems-service/problems/${problemId}`, {
                headers: {
                    Authorization: `Bearer ${auth}`
                }
            });
            setProblems(problems.filter(problem => problem.id !== problemId));
            setFilteredProblems(filteredProblems.filter(problem => problem.id !== problemId));
        } catch (err) {
            setError(err.message);
        }
    };

    const confirmDeleteProblem = (problemId) => {
        setConfirmData({
            action: () => handleDeleteProblem(problemId),
            message: "Are you sure you want to delete this problem?"
        });
        setConfirmAction('delete');
    };

    const handleDeleteUser = async (username) => {
        try {
            await axios.delete(`/auth/${username}`, {
                headers: {
                    Authorization: `Bearer ${auth}`
                }
            });
            setUsers(users.filter(user => user.username !== username));
            setFilteredUsers(filteredUsers.filter(user => user.username !== username));
        } catch (err) {
            setError(err.message);
        }
    };

    const confirmDeleteUser = (username) => {
        setConfirmData({
            action: () => handleDeleteUser(username),
            message: "Are you sure you want to delete this user?"
        });
        setConfirmAction('delete');
    };

    const handleMakeAdmin = async (username) => {
        try {
            await axios.patch(`/auth/${username}/make-admin`, {}, {
                headers: {
                    Authorization: `Bearer ${auth}`
                }
            });
            fetchUsers();
        } catch (err) {
            setError(err.message);
        }
    };

    const confirmMakeAdmin = (username) => {
        setConfirmData({
            action: () => handleMakeAdmin(username),
            message: "Are you sure you want to make this user an Admin?"
        });
        setConfirmAction('make-admin');
    };

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const indexOfLastProblem = currentPage * problemsPerPage;
    const indexOfFirstProblem = indexOfLastProblem - problemsPerPage;
    const currentProblems = filteredProblems.slice(indexOfFirstProblem, indexOfLastProblem);
    const totalProblemPages = Math.ceil(filteredProblems.length / problemsPerPage);

    const indexOfLastUser = currentPage * usersPerPage;
    const indexOfFirstUser = indexOfLastUser - usersPerPage;
    const currentUsers = filteredUsers.slice(indexOfFirstUser, indexOfLastUser);
    const totalUserPages = Math.ceil(filteredUsers.length / usersPerPage);

    const renderContent = () => {
        if (isLoading) return <p>Loading...</p>;
        if (error) return <p>Error: {error}</p>;

        if (activeTab === 'manage-users') {
            return (
                <div>
                    <h3>User List</h3>
                    <input
                        type="text"
                        placeholder="Search Users"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="control-panel-search-bar"
                    />
                    <ul className="control-panel-user-list">
                        {currentUsers.map(user => (
                            <li
                                key={user.username}
                                className="control-panel-user-item"
                                onClick={() => navigate(`/profile/${user.username}`)}
                            >
                                <span>{user.username}</span>
                                <div className="control-panel-user-actions">
                                    {!(user.role === "ADMIN") && (
                                        <button
                                            className="control-panel-make-admin-button"
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                confirmMakeAdmin(user.username);
                                            }}
                                        >
                                            Make Admin
                                        </button>
                                    )}
                                    {user.role === "ADMIN" && <span className="control-panel-admin-star">⭐</span>}
                                    <button
                                        className="control-panel-delete-button"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            confirmDeleteUser(user.username);
                                        }}
                                    >
                                        Delete
                                    </button>
                                </div>
                            </li>
                        ))}
                    </ul>
                    {totalUserPages > 1 && (
                        <div className="control-panel-pagination">
                            {currentPage > 1 && (
                                <button
                                    className="control-panel-pagination-button"
                                    onClick={() => handlePageChange(currentPage - 1)}
                                >
                                    &laquo; Prev
                                </button>
                            )}

                            {Array.from({ length: totalUserPages }, (_, index) => {
                                if (
                                    index + 1 === currentPage ||
                                    index + 1 === 1 ||
                                    index + 1 === totalUserPages ||
                                    (index + 1 >= currentPage - 1 && index + 1 <= currentPage + 1)
                                ) {
                                    return (
                                        <button
                                            key={index + 1}
                                            className={`control-panel-pagination-button ${currentPage === index + 1 ? 'active' : ''}`}
                                            onClick={() => handlePageChange(index + 1)}
                                        >
                                            {index + 1}
                                        </button>
                                    );
                                } else if (
                                    (index + 1 === currentPage - 2 && currentPage - 2 > 1) ||
                                    (index + 1 === currentPage + 2 && currentPage + 2 < totalProblemPages)
                                ) {
                                    return <span key={index + 1} className="control-panel-pagination-ellipsis">...</span>;
                                } else {
                                    return null;
                                }
                            })}

                            {currentPage < totalUserPages && (
                                <button
                                    className="control-panel-pagination-button"
                                    onClick={() => handlePageChange(currentPage + 1)}
                                >
                                    Next &raquo;
                                </button>
                            )}
                        </div>
                    )}
                </div>
            );
        } else if (activeTab === 'manage-problems') {
            return (
                <div>
                    <h3>Problem List</h3>
                    <input
                        type="text"
                        placeholder="Search Problems"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="control-panel-search-bar"
                    />
                    <ul className="control-panel-problem-list">
                        {currentProblems.map((problem) => (
                            <li key={problem.id} className="control-panel-problem-item" onClick={() => handleProblemClick(problem)}>
                                <span className="column title">{problem.problemId.order}. {problem.name}</span>
                                <span className="control-panel-column type">{problem.type}</span>
                                <span className={`control-panel-column difficulty ${problem.difficulty.toLowerCase()}`}>
                                    {problem.difficulty}
                                </span>
                                <span className="control-panel-column topics">{problem.topics.join(', ')}</span>
                                <button
                                    className="control-panel-delete-button"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        confirmDeleteProblem(problem.id);
                                    }}
                                >
                                    Delete
                                </button>
                            </li>
                        ))}
                    </ul>
                    {totalProblemPages > 1 && (
                        <div className="control-panel-pagination">
                            {totalProblemPages > 5 && (
                                <button
                                    className="control-panel-pagination-button"
                                    onClick={() => handlePageChange(currentPage - 1)}
                                    disabled={currentPage === 1}
                                >
                                    &laquo; Prev
                                </button>
                            )}
                            {Array.from({ length: totalProblemPages }, (_, index) => {
                                if (
                                    index + 1 === currentPage ||
                                    index + 1 === 1 ||
                                    index + 1 === totalProblemPages ||
                                    (index + 1 >= currentPage - 1 && index + 1 <= currentPage + 1)
                                ) {
                                    return (
                                        <button
                                            key={index + 1}
                                            className={`control-panel-pagination-button ${currentPage === index + 1 ? 'active' : ''}`}
                                            onClick={() => handlePageChange(index + 1)}
                                        >
                                            {index + 1}
                                        </button>
                                    );
                                } else if (
                                    (index + 1 === currentPage - 2 && currentPage - 2 > 1) ||
                                    (index + 1 === currentPage + 2 && currentPage + 2 < totalProblemPages)
                                ) {
                                    return <span key={index + 1} className="control-panel-pagination-ellipsis">...</span>;
                                } else {
                                    return null;
                                }
                            })}
                            {totalProblemPages > 5 && (
                                <button
                                    className="control-panel-pagination-button"
                                    onClick={() => handlePageChange(currentPage + 1)}
                                    disabled={currentPage === totalProblemPages}
                                >
                                    Next &raquo;
                                </button>
                            )}
                        </div>
                    )}
                </div>
            );
        }
    };

    return (
        <div className="control-panel-container">
            <TopBar />
            <div className="control-panel-tabs-container">
                <div
                    className={`control-panel-tab ${activeTab === 'manage-users' ? 'control-panel-active' : ''}`}
                    onClick={() => setActiveTab('manage-users')}
                >
                    <h3>Manage Users</h3>
                </div>
                <div
                    className={`control-panel-tab ${activeTab === 'manage-problems' ? 'control-panel-active' : ''}`}
                    onClick={() => setActiveTab('manage-problems')}
                >
                    <h3>Manage Problems</h3>
                </div>
            </div>
            <div className="control-panel-content-container">
                {renderContent()}
            </div>
            {confirmAction && confirmData && (
                <Modal
                    message={confirmData.message}
                    onConfirm={() => {
                        confirmData.action();
                        setConfirmAction(null);
                        setConfirmData(null);
                    }}
                    onCancel={() => {
                        setConfirmAction(null);
                        setConfirmData(null);
                    }}
                />
            )}
        </div>
    );
};

export default ControlPanel;
