import {Link} from "react-router-dom";
import React, {useContext} from "react";
import {convertToObject} from "../fetch/fetchGet";
import {AuthContext} from "../App";
import '../../css/tasksScreen.css';
import {NavBar} from "../elements/navBar";

export function TasksPage() {
    return (
        <div>
            <NavBar/>
            <h1 className="task-header">User Tasks</h1>
            <br/>
            <Tasks/>
        </div>
    );
}


export function Tasks() {
    const auth = useContext(AuthContext);
    const userID = auth.userID;
    const tasks = convertToObject(`/api/task/user/${userID}?offset=0&limit=100`, "tasks");

    return (
        <div>

            <div className="task-grid">
                {tasks !== undefined &&
                    tasks.map((task) => (
                        <div key={task.id} className="task-card">
                            <h2 className="task-name">
                                <Link to={`/task/${task.id}`} className="task-link">
                                    {task.name}
                                </Link>
                            </h2>
                            <p className="task-status">Status: {task.status}</p>
                        </div>
                    ))}
            </div>
        </div>
    );
}
