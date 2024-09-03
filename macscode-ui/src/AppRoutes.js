import React from 'react';
import {Routes, Route} from 'react-router-dom';
import Home from './components/homepage/Home';
import Login from './components/Login';
import Signup from './components/Signup';
import PrivateRoute from './components/routes/PrivateRoute';
import Profile from "./components/Profile";
import Problem from "./components/Problem";
import NotFound from "./components/NotFound";
import BlockedRoute from "./components/routes/BlockedRoute";
import UserProfile from "./components/admin/UserProfile";
import ControlPanel from "./components/admin/ControlPanel";
import ProblemCreation from "./components/ProblemCreation";
import ProblemDraftsList from "./components/ProblemDraftsList";

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<Login/>}/>
            <Route path="/signup" element={<Signup/>}/>
            <Route
                path="/"
                element={
                    <PrivateRoute>
                        <Home/>
                    </PrivateRoute>
                }
            />
            <Route path="/profile" element={
                <PrivateRoute>
                    <Profile/>
                </PrivateRoute>
                }
            />
            <Route path="/problem/:course/:order" element={
                    <PrivateRoute>
                        <Problem />
                    </PrivateRoute>
                }
            />
            <Route
                path="/problem-drafts"
                element={
                    <PrivateRoute>
                        <ProblemDraftsList />
                    </PrivateRoute>
                }
            />
            <Route
                path="/problem-creation"
                element={
                    <PrivateRoute>
                        <ProblemCreation />
                    </PrivateRoute>
                }
            />
            <Route
                path="/profile/:username"
                element={
                    <BlockedRoute requiredRole="ADMIN">
                        <UserProfile />
                    </BlockedRoute>
                }
            />
            <Route
                path="/control-panel"
                element={
                    <BlockedRoute requiredRole="ADMIN">
                        <ControlPanel />
                    </BlockedRoute>
                }
            />
            <Route path="*" element={<NotFound />} />
        </Routes>
    );
};

export default AppRoutes;
