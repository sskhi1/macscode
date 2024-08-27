import {useContext, useEffect, useState} from 'react';
import axios from 'axios';
import {AuthContext} from '../AuthContext';
import {jwtDecode} from 'jwt-decode';

const useFetchSubmissions = () => {
    const {auth} = useContext(AuthContext);
    const [submissions, setSubmissions] = useState([]);

    useEffect(() => {
        const fetchSubmissions = async () => {
            if (!auth) return null;

            try {
                const decodedToken = jwtDecode(auth);
                const username = decodedToken.sub;

                const response = await axios.get(`/problems-service/submissions/users/${username}`, {
                    headers: {
                        Authorization: `Bearer ${auth}`
                    }
                });
                setSubmissions(response.data);
            } catch (error) {
                console.error('Error fetching user submissions:', error);
                throw error;
            }
        };

        fetchSubmissions();
    }, [auth]);

    return submissions;
};

export default useFetchSubmissions;