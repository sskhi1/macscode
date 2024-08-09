import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/NotFound.css';
import homeIcon from "../icons/logo192.png";

const NotFound = () => {
    const navigate = useNavigate();

    return (
        <div className="not-found-container">
            <h1>404 - Page Not Found</h1>
            <p>Sorry, the page you are looking for does not exist.</p>
            <img
                src={homeIcon}
                alt="Home"
                className="home-button"
                onClick={() => navigate('/')}
            />
        </div>
    );
};

export default NotFound;
