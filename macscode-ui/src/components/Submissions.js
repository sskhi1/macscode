import React, {useContext, useEffect, useState} from 'react';
import axios from 'axios';
import {AuthContext} from '../AuthContext';
import {Prism as SyntaxHighlighter} from 'react-syntax-highlighter';
import {dracula} from 'react-syntax-highlighter/dist/esm/styles/prism';
import '../styles/Submissions.css';
import {jwtDecode} from "jwt-decode";

const Submissions = ({problemId}) => {
    const {auth} = useContext(AuthContext);
    const [submissions, setSubmissions] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [selectedCode, setSelectedCode] = useState(null);
    const [selectedLanguage, setSelectedLanguage] = useState('java');
    const [showCode, setShowCode] = useState(false);
    const submissionsPerPage = 5;

    useEffect(() => {
        const fetchSubmissions = async () => {
            try {
                const decodedToken = jwtDecode(auth);
                const username = decodedToken.sub;
                const response = await axios.get(`/problems-service/submissions/problem/${problemId}/${username}`, {
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
    }, [auth, problemId]);

    const handleCodeClick = (solutionFileContent, language) => {
        setSelectedCode(solutionFileContent);
        setSelectedLanguage(language);
        setShowCode(true);
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
                    <p className="no-message">You have no submissions for this problem</p>
                ) : (
                    <div className="submissions-list">
                        {currentSubmissions.map(submission => (
                            <div className="submission-item" key={submission.id.toString()}>
                                <div style={{flex: 0.5}}
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
                )}
                {totalPages > 1 && (
                    <div className="pagination">
                        {Array.from({length: totalPages}, (_, index) => (
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

export default Submissions;
