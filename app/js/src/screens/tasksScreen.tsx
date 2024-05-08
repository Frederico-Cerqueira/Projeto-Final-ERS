import {Link} from "react-router-dom";
import React from "react";

export function Tasks() {
    return (
        <div >
            Tasks
            <p><Link to="/task">Task</Link></p>
        </div>
    )
}