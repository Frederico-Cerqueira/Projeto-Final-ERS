import {Link} from "react-router-dom";
import React from "react";
import {useFetchGet} from "../fetch/fetchGet";

export function Tasks() {

    const content = useFetchGet({uri: 'api/task/user/1?offset=0&limit=100'})
    let tasks:Array<any> = []

    if (content !== undefined) {
        tasks = JSON.parse(content).tasks
        console.log(tasks)

    }

    return (
        <div>
            <h1>Tasks</h1>
            <p><Link to="/user">Back to User</Link></p>
            <p><Link to="/task">Task</Link></p>
            <p><Link to="/createTask">CreateTask</Link></p>
            <div>
                {tasks !== undefined && tasks.map(task => (
                    <div key={task.id}>
                        <h2>Task: {task.id} </h2>
                        <p>Name: {task.name}</p>
                        <p>Status: {task.status}</p>
                        <br></br>
                    </div>
                ))}
            </div>
        </div>
    )
}