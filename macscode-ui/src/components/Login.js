import React, { useState, useContext } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../AuthContext';
import '../styles/Login.css';
import '../styles/Loading.css';
import logo from '../icons/M.png';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { setAuth } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/auth-service/auth/login', {
                username,
                password,
            });
            const token = response.data.token;
            setAuth(token);
            localStorage.setItem('token', token);
            navigate('/');
        } catch (error) {
            setError('Invalid username or password');
            console.error('Error logging in', error);
        }
    };

    return (
        <div className="login-container">
            <form onSubmit={handleLogin} className="login-form">
                <img src={logo} alt="Logo" className="login-logo" />
                {error && <div className="error-message">{error}</div>} {}
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Username"
                    className="login-input"
                    required
                />
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                    className="login-input"
                    required
                />
                <button type="submit" className="login-button">Login</button>
                <p className="signup-link">
                    Don't have an account? <a href="/signup">Sign up</a>
                </p>
            </form>
        </div>
    );
};

export default Login;
