import React, {useCallback, useEffect, useState} from 'react';
import '../../styles/Home.css';
import '../../styles/Difficulty.css';
import TypeBanners from "./TypeBanners";
import TopicsGrid from "./TopicsGrid";
import Filters from "./Filters";
import TableHeader from "./TableHeader";
import ProblemList from "./ProblemList";
import TopBar from "../TopBar";
import useFetchSubmissions from "../useFetchSubmissions";

const Home = () => {
    const [problems, setProblems] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [filteredResults, setFilteredResults] = useState([]);
    const [sortConfig, setSortConfig] = useState({key: 'course', direction: 'asc'});
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [selectedType, setSelectedType] = useState('all');
    const [topics, setTopics] = useState([]);
    const [topicCounts, setTopicCounts] = useState('');

    const filteredProblems = filteredResults.filter(problem => {
        return (
            (selectedType === 'all' || problem.type.toLowerCase() === selectedType) &&
            (selectedCategories.length === 0 || selectedCategories.some(topic => problem.topics.includes(topic)))
        );
    });

    const submissions = useFetchSubmissions();

    const sortProblems = useCallback((problems, {key, direction}) => {
        const difficultyOrder = {'easy': 1, 'medium': 2, 'hard': 3};
        const courseOrder = {'KAREL': 1, 'MET': 2, 'ABS': 3};

        if (!key || direction === 'none') return problems;

        return [...problems].sort((a, b) => {
            if (key === 'title') {
                const orderA = a.problemId.order;
                const orderB = b.problemId.order;
                return direction === 'asc' ? orderA - orderB : orderB - orderA;
            } else if (key === 'difficulty') {
                const rankA = difficultyOrder[a.difficulty.toLowerCase()];
                const rankB = difficultyOrder[b.difficulty.toLowerCase()];
                return direction === 'asc' ? rankA - rankB : rankB - rankA;
            } else if (key === 'course') {
                return courseOrder[a.problemId.course] - courseOrder[b.problemId.course]
            } else {
                const itemA = a[key].toLowerCase();
                const itemB = b[key].toLowerCase();
                if (itemA < itemB) return direction === 'asc' ? -1 : 1;
                if (itemA > itemB) return direction === 'asc' ? 1 : -1;
                return 0;
            }
        });
    }, []);

    useEffect(() => {
        const fetchProblems = async () => {
            try {
                const response = await fetch('/problems-service/problems/all');
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                let data = await response.json();
                if (sortConfig.key) {
                    data = sortProblems(data, sortConfig);
                }
                setProblems(data);
                const allTopics = new Set();

                data.forEach(problem => {
                    problem.topics.forEach(topic => allTopics.add(topic));
                });

                setTopics(Array.from(allTopics));
                setTopicCounts(computeTopicCounts(data));
                setFilteredResults(data);

                setIsLoading(false);
            } catch (err) {
                setError(err.message);
                setIsLoading(false);
            }
        };

        fetchProblems();
    }, [sortConfig, sortProblems]);


    const requestSort = (key) => {
        let direction = 'asc';
        if (sortConfig.key === key && sortConfig.direction === 'asc') {
            direction = 'desc';
        } else if (sortConfig.key === key && sortConfig.direction === 'desc') {
            direction = 'none';
        }
        setSortConfig({key, direction});
    };

    const handleBannerClick = (type) => {
        setSelectedType(prevType => prevType === type ? 'all' : type);
    };

    const handleCategoriesClick = (topic) => {
        setSelectedCategories(prevTopics => {
            if (prevTopics.includes(topic)) {
                return prevTopics.filter(t => t !== topic);
            } else {
                return [...prevTopics, topic];
            }
        });
    };

    const getStatus = (problem) => {
        const problemSubmissions = submissions.filter(submission => submission.problem.id === problem.id);
        if (problemSubmissions.some(submission => submission.result.toLowerCase() === 'accepted')) {
            return 'Done';
        } else if (problemSubmissions.length > 0) {
            return 'Attempted';
        } else {
            return 'ToDo';
        }
    };

    const computeTopicCounts = (problems) => {
        const counts = {};
        problems.forEach(problem => {
            problem.topics.forEach(topic => {
                counts[topic] = (counts[topic] || 0) + 1;
            });
        });
        return counts;
    };

    return (
        <div className="container">
            <TopBar/>
            {isLoading ? (
                <div className="loading-container">
                    <div className="spinner"></div>
                    <div className="loading-text">Loading...</div>
                </div>
            ) : error ? (
                <p>Error loading problems: {error}</p>
            ) : (
                <div>
                    <div>
                        <TypeBanners
                            selectedType={selectedType}
                            handleBannerClick={handleBannerClick}/>
                    </div>

                    <div className="filters">
                        <TopicsGrid
                            topicCounts={topicCounts}
                            selectedCategories={selectedCategories}
                            handleCategoriesClick={handleCategoriesClick}
                        />

                        <Filters
                            topics={topics}
                            setFilteredResults={setFilteredResults}
                            problems={problems}
                            getStatus={getStatus}
                        />
                    </div>

                    <TableHeader
                        sortConfig={sortConfig}
                        requestSort={requestSort}
                    />

                    <ProblemList
                        problems={filteredProblems}
                        getStatus={getStatus}
                    />
                </div>
            )}
        </div>
    );
};

export default Home;
