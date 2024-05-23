import React, {useContext, useState} from "react";
import {TaskInputModel} from "../types/TaskInputModel";
import {fetchWrapper} from "../fetch/fetchPost";
import {Link, useNavigate, useParams} from "react-router-dom";
import {CreateTaskForm} from "../forms/taskForms";
import {AuthContext} from "../App";


export function CreateTask() {
    const param = useParams()
    const auth = useContext(AuthContext)
    const navigate = useNavigate();
    const userID = auth.userID
    const robotID = param.id
    console.log(userID, robotID)
    const [name, setName] = useState('');
    //const [userID, setUserID] = useState(0);
    //const [robotID, setRobotID] = useState(0);

    async function clickHandler() {
        const body: TaskInputModel = {name, userID,robotID };
        const uri = '/api/task';

        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success!', jsonData);
            navigate('/tasks')
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
                changeHandlerName={event => setName(event.target.value)}
                clickHandler={clickHandler}
            />
        </div>
    );
}
