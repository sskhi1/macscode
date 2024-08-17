import React from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faUserCircle} from '@fortawesome/free-solid-svg-icons';
import '../styles/TopBar.css';
import logoImage from '../icons/M.png';


const TopBar = () => {
    return (
        <div className="top-bar">
            <nav className="navigation">
                <a href="/" className="logo">
                    <img src={logoImage} alt="Logo"/>
                </a>
            </nav>
            <nav className="navigation">
                <a href="/">Problemset</a>
                <a href="/discuss">Discuss</a>
            </nav>
            <a href="/profile" className="user-profile">
                <FontAwesomeIcon icon={faUserCircle} size="2x" className="profile-icon"/>
            </a>
        </div>
    );
};

export default TopBar;
