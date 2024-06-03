import React from 'react'
import {Link} from "react-router-dom";


export function InitialScreen() {
    return (
        <div>
            <p> HOME </p>
            <p><Link to="/createUser">CreateUser</Link></p>
            <p><Link to="/login">Login</Link></p>
        </div>
    )
}
