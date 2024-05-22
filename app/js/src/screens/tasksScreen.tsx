import {Link} from "react-router-dom";
import React, {useContext} from "react";
import {convertToObject} from "../fetch/fetchGet";
import {AuthContext} from "../App";

export function Tasks() {
    const auth = useContext(AuthContext)
    const userID = auth.userID
    const tasks = convertToObject('/api/task/user/'+userID+'?offset=0&limit=100', 'tasks')

    return (
        <div>
            <h1>Tasks</h1>
            <p><Link to={"/user/"+userID}>Back to User</Link></p>
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