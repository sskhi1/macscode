import React, {useContext, useEffect, useState} from 'react';
import axios from 'axios';
import {AuthContext} from '../AuthContext';
import {jwtDecode} from 'jwt-decode';
import {useNavigate} from 'react-router-dom';
import {Prism as SyntaxHighlighter} from 'react-syntax-highlighter';
import {dracula} from 'react-syntax-highlighter/dist/esm/styles/prism';
import '../styles/Profile.css';
import '../styles/Loading.css';
import TopBar from "./TopBar";

const Profile = () => {
    const {auth} = useContext(AuthContext);
    const [userDetails, setUserDetails] = useState(null);
    const [submissions, setSubmissions] = useState([]);
    const [topicsCount, setTopicsCount] = useState({});
    const [currentPage, setCurrentPage] = useState(1);
    const [selectedCode, setSelectedCode] = useState(null);
    const [selectedLanguage, setSelectedLanguage] = useState('java');
    const [showCode, setShowCode] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [updatedName, setUpdatedName] = useState('');
    const [updatedEmail, setUpdatedEmail] = useState('');
    const navigate = useNavigate();
    const submissionsPerPage = 10;

    useEffect(() => {
        const fetchUserDetails = async () => {
            try {
                const decodedToken = jwtDecode(auth);
                const username = decodedToken.sub;

                const userResponse = await axios.get(`/auth-service/auth/users/${username}`, {
                    headers: {
                        Authorization: `Bearer ${auth}`
                    }
                });
                setUserDetails(userResponse.data);
                setUpdatedName(userResponse.data.name);
                setUpdatedEmail(userResponse.data.email);

                const submissionsResponse = await axios.get(`/problems-service/submissions/users/${username}`, {
                    headers: {
                        Authorization: `Bearer ${auth}`
                    }
                });
                setSubmissions(submissionsResponse.data);
                calculateTopicsCount(submissionsResponse.data);
            } catch (error) {
                console.error('Error fetching data', error);
            }
        };

        if (auth) {
            fetchUserDetails();
        }
    }, [auth]);

    const calculateTopicsCount = (submissions) => {
        const topicCounts = {};
        const uniqueProblemIds = new Set();

        submissions.forEach(submission => {
            if (submission.result === 'ACCEPTED') {
                if (!uniqueProblemIds.has(submission.problem.id)) {
                    uniqueProblemIds.add(submission.problem.id);
                    const topics = new Set(submission.problem.topics);

                    topics.forEach(topic => {
                        topicCounts[topic] = (topicCounts[topic] || 0) + 1;
                    });
                }
            }
        });

        setTopicsCount(topicCounts);
    };

    const handleCodeClick = async (solutionFileContent, language) => {
        try {
            setSelectedCode(solutionFileContent);
            setSelectedLanguage(language);
            setShowCode(true);
        } catch (error) {
            console.error('Error fetching code', error);
        }
    };

    const handleProblemClick = (problem) => {
        navigate(`/problem/${problem.problemId.course}/${problem.problemId.order}`);
    };

    const countAcceptedCodes = (type) => {
        const uniqueProblemIds = new Set();

        submissions.forEach(submission => {
            if (submission.result === 'ACCEPTED' && submission.problem.problemId.course === type) {
                uniqueProblemIds.add(submission.problem.id);
            }
        });

        return uniqueProblemIds.size;
    };

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleSaveClick = async () => {
        try {
            const decodedToken = jwtDecode(auth);
            const username = decodedToken.sub;

            await axios.put(`/auth-service/auth/users/update/${username}`, {
                name: updatedName,
                email: updatedEmail,
            }, {
                headers: {
                    Authorization: `Bearer ${auth}`
                }
            });

            setUserDetails((prevState) => ({
                ...prevState,
                name: updatedName,
                email: updatedEmail,
            }));
            setIsEditing(false);
        } catch (error) {
            console.error('Error updating profile', error);
        }
    };

    const indexOfLastSubmission = currentPage * submissionsPerPage;
    const indexOfFirstSubmission = indexOfLastSubmission - submissionsPerPage;
    const currentSubmissions = submissions.slice(indexOfFirstSubmission, indexOfLastSubmission);
    const totalPages = Math.ceil(submissions.length / submissionsPerPage);

    const handleClickOutside = (event) => {
        if (event.target.classList.contains('code-popup')) {
            setShowCode(false);
        }
    };

    useEffect(() => {
        if (showCode) {
            document.addEventListener('mousedown', handleClickOutside);
        } else {
            document.removeEventListener('mousedown', handleClickOutside);
        }

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [showCode]);

    if (!userDetails) {
        return <div className="loading-container">
            <div className="spinner"></div>
            <div className="loading-text">Loading...</div>
        </div>;
    }

    const methodologyCount = countAcceptedCodes('MET');
    const abstractionsCount = countAcceptedCodes('ABS');

    return (
        <div className="profile-page">
            <TopBar/>
            <div className="profile-container">
                <div className="profile-info-container">
                    <div className="profile-header">
                        {isEditing ? (
                            <div className="edit-profile-container">
                                <input
                                    type="text"
                                    value={updatedName}
                                    onChange={(e) => setUpdatedName(e.target.value)}
                                    placeholder="Name"
                                />
                                <input
                                    type="email"
                                    value={updatedEmail}
                                    onChange={(e) => setUpdatedEmail(e.target.value)}
                                    placeholder="Email"
                                />
                                <button onClick={handleSaveClick}>Save</button>
                            </div>
                        ) : (
                            <div className="profile-header-info">
                                <h2>{userDetails.username}</h2>
                                <p>Name: {userDetails.name}</p>
                                <p>Email: {userDetails.email}</p>
                                <button onClick={handleEditClick}>Edit Profile</button>
                            </div>
                        )}
                    </div>
                    <div className="statistics-container">
                        <h3>Statistics</h3>
                        <p className="statistics-text">
                            <span className="methodology-label">Methodology:</span> <span
                            className="count">{methodologyCount}</span> {methodologyCount === 1 ? 'Problem Solved' : 'Problems Solved'}
                        </p>
                        <p className="statistics-text">
                            <span className="abstractions-label">Abstractions:</span> <span
                            className="count">{abstractionsCount}</span> {abstractionsCount === 1 ? 'Problem Solved' : 'Problems Solved'}
                        </p>
                        {
                            Object.keys(topicsCount).length > 0 &&
                            <h4>Topics</h4>
                        }
                        <div className="topics-container">
                            {Object.keys(topicsCount).map((topic) => (
                                <span key={topic} className="topic-box">
                                    {topic}: x{topicsCount[topic]}
                                </span>
                            ))}
                        </div>
                    </div>
                </div>
                <div className="submissions-container">
                    <h3>Recent Submissions</h3>
                    {submissions.length === 0 ? (
                        <p className="no-message">No submissions available</p>
                    ) : (
                        <>
                            <div className="submissions-list">
                                {currentSubmissions.map(submission => (
                                    <div
                                        className="submission-item"
                                        key={submission.id.toString()}
                                    >
                                        <div
                                            className="problem-name"
                                            onClick={() => handleProblemClick(submission.problem)}
                                            style={{cursor: 'pointer', color: '#ffb700'}}
                                        >
                                            {submission.problem.name}
                                        </div>
                                        <div
                                            className={`result ${submission.result === 'ACCEPTED' ? 'accepted' : 'rejected'}`}>
                                            {submission.result}
                                        </div>
                                        <div className="date">
                                            {new Date(submission.submissionDate).toLocaleString('en-US', {
                                                dateStyle: 'short',
                                                timeStyle: 'short'
                                            })}
                                        </div>
                                        <button
                                            className="view-code-button"
                                            onClick={() => handleCodeClick(submission.solutionFileContent, "java")}
                                        >
                                            View Code
                                        </button>
                                    </div>
                                ))}
                            </div>
                            {totalPages > 1 && (
                                <div className="pagination">
                                    {currentPage > 1 && (
                                        <button
                                            className="pagination-button"
                                            onClick={() => handlePageChange(currentPage - 1)}
                                        >
                                            &laquo;
                                        </button>
                                    )}
                                    {Array.from({length: totalPages}, (_, index) => {
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
                                                return <span style={{color: 'white'}}>...</span>;
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
                            )}
                        </>
                    )}
                </div>
            </div>
            {showCode && (
                <div className="code-popup">
                    <div className="code-popup-content">
                        <SyntaxHighlighter language={selectedLanguage} style={dracula}>
                            {selectedCode}
                        </SyntaxHighlighter>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Profile;