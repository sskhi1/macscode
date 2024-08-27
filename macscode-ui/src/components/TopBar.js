import React, { useContext, useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle, faSignOutAlt } from '@fortawesome/free-solid-svg-icons';
import '../styles/TopBar.css';
import logoImage from '../icons/M.png';
import { AuthContext } from '../AuthContext';
import {jwtDecode} from 'jwt-decode';
import { useNavigate } from 'react-router-dom';

const TopBar = () => {
    const { auth, setAuth } = useContext(AuthContext);
    const [username, setUsername] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        if (auth) {
            const decodedToken = jwtDecode(auth);
            setUsername(decodedToken.sub);
        }
    }, [auth]);

    const handleLogout = () => {
        setAuth(null);
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div className="top-bar">
            <nav className="navigation">
                <a href="/" className="logo">
                    <img src={logoImage} alt="Logo" />
                </a>
            </nav>
            <nav className="navigation">
                <a href="/">Problemset</a>
            </nav>
            <div className="user-actions">
                <a href="/profile" className="user-profile">
                    <FontAwesomeIcon icon={faUserCircle} size="2x" className="profile-icon" />
                    <span className="username">{username}</span>
                </a>
                <button className="logout-button" onClick={handleLogout}>
                    <FontAwesomeIcon icon={faSignOutAlt} size="lg" className="logout-icon" />
                </button>
            </div>
        </div>
    );
};

export default TopBar;
