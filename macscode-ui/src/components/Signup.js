import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/Signup.css';
import { FaEye, FaEyeSlash } from 'react-icons/fa';

const Signup = () => {
    const [name, setName] = useState('');
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSignup = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }
        try {
            await axios.post('/auth-service/auth/signup', {
                name,
                username,
                email,
                password
            });
            navigate('/login');
        } catch (error) {
            console.error('Error signing up', error);
            setError('Error signing up. Please check your details and try again.');
        }
    };

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const toggleConfirmPasswordVisibility = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    return (
        <div className="signup-container">
            <form onSubmit={handleSignup} className="signup-form">
                <h2 className="signup-title">Sign Up</h2>
                {error && <div className="error-message">{error}</div>}
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Name"
                    className="signup-input"
                    required
                />
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Username"
                    className="signup-input"
                    required
                />
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                    className="signup-input"
                    required
                />
                <div className="password-container">
                    <input
                        type={showPassword ? "text" : "password"}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Password"
                        className="signup-input"
                        required
                    />
                    <div className="eye-icon" onClick={togglePasswordVisibility}>
                        {showPassword ? <FaEyeSlash /> : <FaEye />}
                    </div>
                </div>
                <div className="password-container">
                    <input
                        type={showConfirmPassword ? "text" : "password"}
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="Confirm Password"
                        className="signup-input"
                        required
                    />
                    <div className="eye-icon" onClick={toggleConfirmPasswordVisibility}>
                        {showConfirmPassword ? <FaEyeSlash /> : <FaEye />}
                    </div>
                </div>
                <button type="submit" className="signup-button">Sign Up</button>
                <p className="login-link">
                    Already have an account? <a href="/login">Log in</a>
                </p>
            </form>
        </div>
    );
};

export default Signup;
