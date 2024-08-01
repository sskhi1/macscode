import React, { useContext } from 'react';
import { AuthContext } from '../AuthContext';

const Home = () => {
    const { auth } = useContext(AuthContext);

    return (
        <div>
            <h1>Welcome to the Home Page</h1>
            {auth ? <p>You are logged in!</p> : <p>Please log in or sign up.</p>}
        </div>
    );
};

export default Home;
