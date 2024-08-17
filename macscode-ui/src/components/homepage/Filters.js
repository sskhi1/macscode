import React, {useEffect, useState} from 'react';
import Select from 'react-select';
import '../../styles/Home.css';
import {IoMdClose} from "react-icons/io";
import {GoSearch} from "react-icons/go";

const Filters = ({
                     topics,
                     setFilteredResults,
                     problems,
                     getStatus
                 }) => {
    const [selectedTopics, setSelectedTopics] = useState([]);
    const [selectedStatus, setSelectedStatus] = useState('all');
    const [selectedDifficulty, setSelectedDifficulty] = useState('all');
    const [searchTerm, setSearchTerm] = useState('');
    const [isActive, setIsActive] = useState(false);

    const topicOptions = topics.map(topic => ({value: topic, label: topic}));

    const difficultyOptions = [
        {value: 'all', label: 'Difficulty'},
        {value: 'easy', label: 'Easy'},
        {value: 'medium', label: 'Medium'},
        {value: 'hard', label: 'Hard'}
    ];

    const statusOptions = [
        {value: 'all', label: 'Status'},
        {value: 'done', label: 'Done'},
        {value: 'attempted', label: 'Attempted'},
        {value: 'todo', label: 'ToDo'}
    ];

    useEffect(() => {
        filterProblems();
    }, [selectedTopics, selectedStatus, selectedDifficulty, searchTerm]);

    const filterProblems = () => {
        const filtered = problems.filter(problem => {
            const status = getStatus(problem);
            return (
                (selectedTopics.length === 0 || selectedTopics.every(topic => problem.topics.includes(topic))) &&
                (selectedStatus === 'all' || status.toLowerCase() === selectedStatus) &&
                (selectedDifficulty === 'all' || problem.difficulty.toLowerCase() === selectedDifficulty) &&
                problem.name.toLowerCase().includes(searchTerm.toLowerCase())
            );
        });
        setFilteredResults(filtered);
    };

    const handleIconClick = () => {
        setIsActive(!isActive);
    };

    const handleInputBlur = (e) => {
        if (searchTerm.trim() === '') {
            setIsActive(false);
        }
    };

    const handleTopicChange = (selectedOptions) => {
        setSelectedTopics(selectedOptions ? selectedOptions.map(option => option.value) : []);
    };

    const handleStatusChange = selectedOption => {
        setSelectedStatus(selectedOption.value);
    };

    const handleDifficultyChange = selectedOption => {
        setSelectedDifficulty(selectedOption.value);
    };

    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value.toLowerCase());
    };

    return (
        <div className="filters">
            <div className="multi-select">
                <Select
                    isMulti
                    name="topics"
                    options={topicOptions}
                    className="select-control multi-select"
                    classNamePrefix="select"
                    onChange={handleTopicChange}
                    placeholder="Filter by topics..."
                    isSearchable={true}
                />
            </div>

            <div className="controls-row">
                <div className="control-item">
                    <Select
                        value={statusOptions.find(option => option.value === selectedStatus)}
                        onChange={handleStatusChange}
                        options={statusOptions}
                        className="select-control"
                        classNamePrefix="select"
                        isSearchable={true}
                    />
                </div>
                <div className="control-item">
                    <Select
                        value={difficultyOptions.find(option => option.value === selectedDifficulty)}
                        onChange={handleDifficultyChange}
                        options={difficultyOptions}
                        className="select-control"
                        classNamePrefix="select"
                        isSearchable={true}
                    />
                </div>

                <div className={`search-container ${isActive ? 'active' : ''}`}>
                    <button type="button" onClick={handleIconClick}>
                        {isActive ? <IoMdClose/> : <GoSearch/>}
                    </button>
                    <input
                        type="search"
                        value={searchTerm}
                        placeholder="Search by title..."
                        onChange={handleSearchChange}
                        onBlur={handleInputBlur}
                    />
                </div>
            </div>
        </div>
    );
};

export default Filters;
