import React, {useEffect, useState, useContext} from 'react';
import axios from 'axios';
import {AuthContext} from '../../AuthContext';
import {Prism as SyntaxHighlighter} from 'react-syntax-highlighter';
import {dracula} from 'react-syntax-highlighter/dist/esm/styles/prism';
import '../../styles/Submissions.css';
import {useNavigate} from "react-router-dom";

const AllSubmissions = ({problemId}) => {
    const {auth} = useContext(AuthContext);
    const [submissions, setSubmissions] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [selectedCode, setSelectedCode] = useState(null);
    const [selectedLanguage, setSelectedLanguage] = useState('java');
    const [showCode, setShowCode] = useState(false);
    const submissionsPerPage = 10;
    const navigate = useNavigate();

    useEffect(() => {
        const fetchSubmissions = async () => {
            try {
                const response = await axios.get(`/problems-service/submissions/problems/${problemId}`, {
                    headers: {
                        Authorization: `Bearer ${auth}`
                    }
                });
                setSubmissions(response.data);
            } catch (error) {
                console.error('Error fetching submissions', error);
            }
        };

        if (auth) {
            fetchSubmissions();
        }
    }, [auth]);

    const handleCodeClick = (solutionFileContent, language) => {
        setSelectedCode(solutionFileContent);
        setSelectedLanguage(language);
        setShowCode(true);
    };

    const handleUsernameClick = (username) => {
        navigate(`/profile/${username}`)
    };

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
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

    return (
        <div className="submissions-page">
            <div className="submissions-container">
                <h3>Submissions</h3>
                {submissions.length === 0 ? (
                    <p className="no-submissions-message">There are No Submissions on This Problem</p>
                ) : (
                    <div className="submissions-list">
                        {currentSubmissions.map(submission => (
                            <div className="submission-item" key={submission.id.toString()}>
                                <div
                                    className="submitter-username"
                                    onClick={() => handleUsernameClick(submission.submitterUsername)}
                                >
                                    {submission.submitterUsername}
                                </div>
                                <div className={`result ${submission.result === 'ACCEPTED' ? 'accepted' : 'rejected'}`}>
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
                )}
                {totalPages > 1 && (
                    <div className="pagination">
                        {Array.from({ length: totalPages }, (_, index) => (
                            <button
                                key={index + 1}
                                className={`pagination-button ${currentPage === index + 1 ? 'active' : ''}`}
                                onClick={() => handlePageChange(index + 1)}
                            >
                                {index + 1}
                            </button>
                        ))}
                    </div>
                )}
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

export default AllSubmissions;
