import {Link} from "react-router-dom";
import React from "react";
import {convertToObject} from "../fetch/fetchGet";

export function Tasks() {

    const tasks = convertToObject('api/task/user/1?offset=0&limit=100', 'tasks')

    return (
        <div>
            <h1>Tasks</h1>
            <p><Link to="/user">Back to User</Link></p>
            <p><Link to="/createTask">CreateTask</Link></p>
            <div>
                {tasks !== undefined && tasks.map(task => (
                    <div key={task.id}>
                        <h2><Link to={`/task/`+task.id}>{task.name}</Link></h2>
                        <p>Status: {task.status}</p>
                        <br></br>
                    </div>
                ))}
            </div>
        </div>
    )
}