
import React from "react";
import {Link} from "react-router-dom";

export function User() {
    return (
        <div >
            <p><Link to="/robots">Robots</Link></p>
            <p><Link to="/tasks">Tasks</Link></p>
        </div>
    )
}