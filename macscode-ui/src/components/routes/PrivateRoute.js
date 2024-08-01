import React, {useContext} from 'react';
import {Navigate} from 'react-router-dom';
import {AuthContext} from '../../AuthContext';
import '../../styles/Loading.css';

const PrivateRoute = ({ children }) => {
    const { auth, loading } = useContext(AuthContext);

    if (loading) {
        return (
            <div className="loading-container">
                <div className="spinner"></div>
                <div className="loading-text">Loading...</div>
            </div>
        );
    }

    if (auth) {
        return children;
    } else {
        return <Navigate to="/login" />;
    }
};

export default PrivateRoute;
