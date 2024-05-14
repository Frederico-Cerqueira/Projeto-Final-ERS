import React, {useState} from "react";
import {TaskInputModel} from "../types/TaskInputModel";
import {fetchWrapper} from "../fetch/fetchPost";
import {Link} from "react-router-dom";
import {CreateTaskForm} from "../forms/taskForms";


export function CreateTask() {
    const [name, setName] = useState('');
    const [userID, setUserID] = useState(0);
    const [robotID, setRobotID] = useState(0);

    async function clickHandler() {
        const body: TaskInputModel = {name, userID, robotID};
        const uri = 'api/task';
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success!', jsonData);
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div>
            <h1>Create Task</h1>
            <p><Link to="/tasks">Back to Tasks</Link></p>
            <CreateTaskForm
                name={name}
                userID={userID}
                robotID={robotID}
                changeHandlerName={(event) => setName(event.target.value)}
                changeHandlerUserID={(event) => setUserID(Number(event.target.value))}
                changeHandlerRobotID={(event) => setRobotID(Number(event.target.value))}
                clickHandler={clickHandler}
            />
        </div>
    );
}
