import React from "react";
import {Link} from "react-router-dom";

export function Robot() {
    return (
        <div >
            Robot
            <p><Link to="/robots">Robots</Link></p>
            <p><Link to="/createRobot">Create Robot</Link></p>
            <p><Link to="/area">Area</Link></p>
            <p><Link to="/time">Time</Link></p>
        </div>
    )
}