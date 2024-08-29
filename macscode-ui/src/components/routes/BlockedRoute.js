import React, {useContext} from 'react';
import {Navigate} from 'react-router-dom';
import {AuthContext} from '../../AuthContext';
import {jwtDecode} from 'jwt-decode';
import NotFound from "../NotFound";

const BlockedRoute = ({children, requiredRole}) => {
    const {auth} = useContext(AuthContext);

    if (!auth) {
        return <Navigate to="/login"/>;
    }

    const decodedToken = jwtDecode(auth);
    const userRole = decodedToken.role;

    if (requiredRole && userRole !== requiredRole) {
        return <NotFound />;
    }

    return children;
};

export default BlockedRoute;
