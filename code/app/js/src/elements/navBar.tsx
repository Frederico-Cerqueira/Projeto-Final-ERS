import React, {useContext} from 'react';
import {Link} from 'react-router-dom';
import {AuthContext} from "../App";

export function NavBar() {
    const auth = useContext(AuthContext)
    const userID = auth.userID
    return (
        <div className="nav">
            <Link className="home" to={"/user/" + userID}>HOME</Link>
            <span className="spacer"></span>
            <Link className="nav-button" to="/robots">Robots</Link>
            <Link className="nav-button" to="/tasks">Tasks</Link>
        </div>
    );
}
