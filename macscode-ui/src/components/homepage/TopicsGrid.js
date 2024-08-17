import React from 'react';
import '../../styles/Home.css';

const TopicsGrid = ({topicCounts, selectedCategories, handleCategoriesClick}) => {
    return (
        <div className="topics-grid">
            {Object.entries(topicCounts).map(([topic, count]) => (
                <div key={topic}
                     className={`topic-item ${selectedCategories.includes(topic) ? 'selected' : ''}`}
                     onClick={() => handleCategoriesClick(topic)}>
                    <span className="topic-label">{topic}</span>
                    <span className="topic-count">{count}</span>
                </div>
            ))}
        </div>
    );
};

export default TopicsGrid;
