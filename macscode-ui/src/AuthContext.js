import React, { createContext, useState, useEffect, useCallback } from 'react';
import { jwtDecode } from 'jwt-decode';

export const AuthContext = createContext({
    auth: null,
    setAuth: () => {}
});

export const AuthProvider = ({ children }) => {
    const [auth, setAuth] = useState(null);
    const [loading, setLoading] = useState(true);

    const checkToken = useCallback((token) => {
        try {
            const decodedToken = jwtDecode(token);
            const now = Date.now().valueOf() / 1000;
            const buffer = 30;
            return decodedToken.exp > (now + buffer);
        } catch (error) {
            console.error('Failed to decode JWT', error);
            return false;
        }
    }, []);

    useEffect(() => {
        const storedToken = localStorage.getItem('token');
        if (storedToken && checkToken(storedToken)) {
            setAuth(storedToken);
        } else {
            localStorage.removeItem('token');
            setAuth(null);
        }
        setLoading(false);
    }, [checkToken]);

    return (
        <AuthContext.Provider value={{ auth, setAuth }}>
            {loading ? (
                <div className="loading-container">
                    <div className="spinner"></div>
                    <div className="loading-text">Loading...</div>
                </div>
            ) : (
                children
            )}
        </AuthContext.Provider>
    );
};
