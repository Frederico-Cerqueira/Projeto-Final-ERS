import React from "react";
import {Link} from "react-router-dom";

export function Robots() {
    return (
        <div >
            Robots
            <p><Link to="/user">User</Link></p>
            <p><Link to="/robot">Robot</Link></p>
        </div>
    )
}