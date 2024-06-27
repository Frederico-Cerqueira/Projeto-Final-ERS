import React from 'react';
import {Link} from 'react-router-dom';

export function InitialNavBar() {
    return (
        <div className="nav">
            <a className="home" href="/">HOME</a>
            <span className="spacer"></span>
            <Link className="auth-button" to="/login">Log in</Link>
        </div>
    );
}
