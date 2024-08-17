import React from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faCaretDown, faCaretUp, faSort} from '@fortawesome/free-solid-svg-icons';
import '../../styles/Home.css';

const TableHeader = ({sortConfig, requestSort}) => {
    return (
        <div className="table-header">
            <span className="header-item">Status</span>
            <span onClick={() => requestSort('title')}>
                Title
                {sortConfig.key === 'title' && sortConfig.direction !== 'none' ? (
                    sortConfig.direction === 'asc' ? (
                        <FontAwesomeIcon icon={faCaretUp} className="sort-icon"/>
                    ) : (
                        <FontAwesomeIcon icon={faCaretDown} className="sort-icon"/>
                    )
                ) : <FontAwesomeIcon icon={faSort} className="sort-icon"/>}
            </span>
            <span onClick={() => requestSort('type')}>
                Type
                {sortConfig.key === 'type' && sortConfig.direction !== 'none' ? (
                    sortConfig.direction === 'asc' ? (
                        <FontAwesomeIcon icon={faCaretUp} className="sort-icon"/>
                    ) : (
                        <FontAwesomeIcon icon={faCaretDown} className="sort-icon"/>
                    )
                ) : <FontAwesomeIcon icon={faSort} className="sort-icon"/>}
            </span>
            <span onClick={() => requestSort('difficulty')}>
                Difficulty
                {sortConfig.key === 'difficulty' && sortConfig.direction !== 'none' ? (
                    sortConfig.direction === 'asc' ? (
                        <FontAwesomeIcon icon={faCaretUp} className="sort-icon"/>
                    ) : (
                        <FontAwesomeIcon icon={faCaretDown} className="sort-icon"/>
                    )
                ) : <FontAwesomeIcon icon={faSort} className="sort-icon"/>}
            </span>
            <span className="header-item">Topics</span>
        </div>
    );
};

export default TableHeader;