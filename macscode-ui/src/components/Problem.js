import React, { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import ProblemDetails from './ProblemDetails';
import SolutionTemplate from './SolutionTemplate';
import TestCases from './TestCases';
import ResultsModal from './ResultsModal';
import '../styles/Problem.css';
import { Client } from '@stomp/stompjs';
import TopBar from "./TopBar";
import Comments from "./Comments";

const Problem = () => {
    const { course, order } = useParams();
    const [problem, setProblem] = useState(null);
    const [error, setError] = useState('');
    const [code, setCode] = useState('');
    const [testCases, setTestCases] = useState([]);
    const [selectedTestCase, setSelectedTestCase] = useState(null);
    const [results, setResults] = useState([]);
    const [showResults, setShowResults] = useState(false);
    const [hasSubmitted, setHasSubmitted] = useState(false);
    const [responseReceived, setResponseReceived] = useState(false);

    const clientRef = useRef(null);
    const discussionRef = useRef(null); // Ref for the discussion section

    useEffect(() => {
        clientRef.current = new Client({
            brokerURL: 'ws://localhost:8080/websocket-endpoint/websocket',
            reconnectDelay: 5000,
            onConnect: () => {
                console.log('Connected to WebSocket');

                clientRef.current.subscribe('/topic/runResult', (message) => {
                    const runResults = JSON.parse(message.body);
                    setResults(runResults);
                    setResponseReceived(true);
                    setShowResults(true);
                });

                clientRef.current.subscribe('/topic/submitResult', (message) => {
                    const submitResults = JSON.parse(message.body);
                    setResults(submitResults);
                    setResponseReceived(true);
                    setShowResults(true);
                });
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            },
        });

        clientRef.current.activate();

        return () => {
            clientRef.current.deactivate();
        };
    }, []);

    useEffect(() => {
        const fetchProblem = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/problems/${course}/${order}`);
                setProblem(response.data);
                setCode(response.data.solutionFileTemplate);
                setTestCases(response.data.publicTestCases || []);
                setSelectedTestCase(response.data.publicTestCases[0]);
            } catch (error) {
                console.error('Error fetching problem', error);
                setError('Error fetching problem details. Please try again later.');
            }
        };

        fetchProblem();
    }, [course, order]);

    const handleCodeChange = (newCode) => {
        setCode(newCode);
    };

    const handleSubmit = () => {
        if (!problem) return;

        clientRef.current.publish({
            destination: '/app/submitSolution',
            body: JSON.stringify({
                problemId: problem.id,
                solution: code,
            }),
        });
        setHasSubmitted(true);
        setResponseReceived(false);
    };

    const handleRun = () => {
        if (!problem) return;

        clientRef.current.publish({
            destination: '/app/runSolution',
            body: JSON.stringify({
                problemId: problem.id,
                solution: code,
            }),
        });
        setHasSubmitted(true);
        setResponseReceived(false);
    };

    const handleCloseResults = () => {
        setShowResults(false);
    };

    const scrollToDiscussion = () => {
        const discussionElement = discussionRef.current;

        discussionElement.style.display = 'block';

        const topOffset = -10;
        const elementPosition = discussionElement.getBoundingClientRect().top + window.scrollY;
        const offsetPosition = elementPosition - topOffset;

        window.scrollTo({
            top: offsetPosition,
            behavior: 'smooth'
        });
    };

    const handleTestCaseSelect = (testCase) => {
        setSelectedTestCase(testCase);
    };

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    if (!problem) {
        return <div>Loading...</div>;
    }

    return (
        <div className="problem-container">
            <TopBar/>
            <div className="content-container">
                <div className="problem-left">
                    <ProblemDetails problem={problem} selectedTestCase={selectedTestCase}/>
                </div>
                <div className="problem-right">
                    <div className="problem-right-upper">
                        <SolutionTemplate
                            solutionFileTemplate={code}
                            onChange={handleCodeChange}
                        />
                    </div>
                    <div className="problem-right-lower">
                        <TestCases testCases={testCases} onSelect={handleTestCaseSelect}/>
                        <div className="button-container">
                            <button className="run-button" onClick={handleRun}>
                                Run
                            </button>
                            <button className="submit-button" onClick={handleSubmit}>
                                Submit
                            </button>
                            <button
                                className={`view-results-button ${hasSubmitted && responseReceived ? 'visible' : ''}`}
                                onClick={() => setShowResults(true)}
                            >
                                View Results
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <button className="scroll-button" onClick={scrollToDiscussion}>
                Go to Comments
            </button>
            <br/>
            <ResultsModal
                show={showResults}
                results={results}
                onClose={handleCloseResults}
            />
            <div ref={discussionRef} className="discussion-section">
                <Comments problemId={problem.id} />
            </div>
        </div>
    );
};

export default Problem;
