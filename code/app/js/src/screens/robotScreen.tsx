import React, {useContext, useState} from 'react';
import {Link, useNavigate, useParams} from "react-router-dom";
import {fetchWrapper} from "../fetch/fetchPost";
import {RobotUpdateInputModel} from "../types/robotInputModel";
import {DeleteButton} from "../elements/deteleButton";
import {useFetchGet, usePaginatedFetch} from "../fetch/fetchGet";
import {NavBar} from "../elements/navBar";
import '../../css/robotScreen.css';
import {AuthContext} from "../App";
import {TaskInputModel} from "../types/taskInputModel";
import {CreateTaskForm} from "../forms/taskForms";

export function Robot() {
    const [robot, setRobot] = useState(null);

    const param = useParams();
    const navigate = useNavigate();
    const robotID = param.id;
    const uri = '/api/robot/' + robotID;

    useFetchGet(uri, setRobot);

    async function fetchDeleteRobot() {
        const uri = '/api/robot/' + robotID;
        try {
            await fetchWrapper(uri, 'DELETE', {});
            navigate('/robots');
        } catch (error) {
            navigate('/error')
        }
    }

    return (
        <div>
            <NavBar/>
            <br/>
            {robot && (
                <div className="robot-details">
                    <h1 className="robot-name">{robot.name}</h1>
                    <div className="robot-info">
                        <p className="robot-info-item">Status: {robot.status}</p>
                        <p className="robot-info-item">Characteristics: {robot.characteristics}</p>
                    </div>
                    <div className="robot-action-buttons">
                        <DeleteButton onClick={fetchDeleteRobot}/>
                    </div>
                </div>
            )}
            <br/>
            <p className="task-header">Tasks:</p>
            <Tasks robotID={robotID}/>
        </div>
    )
}

function UpdateRobot({robotID, setRobot}) {
    const [status, setStatus] = useState('');
    const [error, setError] = useState(false);

    const body: RobotUpdateInputModel = {status};
    const uri = '/api/robot/update/' + robotID;

    async function clickHandler() {
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            if (jsonData.id) {
                setRobot(jsonData);
            } else {
                setError(true);
            }
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div className="robot-update-form">
            <input
                type="text"
                value={status}
                onChange={event => setStatus(event.target.value)}
                placeholder="Update Status"
            />
            <button className="robot-submit-button" onClick={clickHandler}>Submit</button>
            {error && <div
                className="error-message">{"The status can only to 'available', 'busy', 'maintenance', 'unavailable', 'charging', 'error'"}</div>}
        </div>
    );
}

function Tasks({robotID}: { robotID: string }) {
    const [showForm, setShowForm] = useState(false);
    const limit = 10;


    const toggleForm = () => setShowForm(!showForm);
    const renderTask = (task) => (
            <div key={task.id} className="task-card">
                <h2 className="task-name">
                    <Link to={`/task/${task.id}`} className="task-link">
                        {task.name}
                    </Link>
                </h2>
                <p className="task-status">Status: {task.status}</p>
            </div>
        );
    const {data, hasMore, loadMore} = usePaginatedFetch(`/api/task/robot/${robotID}`, limit, "tasks");
    return (
        <div>
            <div className="task-grid">
                {data.map((item) => renderTask(item))}
                {!showForm && (
                    <div className="add-task-card" onClick={toggleForm}>
                        <span className="add-task-icon">+</span>
                    </div>
                )}
                {showForm && (
                    <div className="add-task-form">
                        <CreateTask/>
                    </div>
                )}
                <PaginatedTasks data={data} hasMore={hasMore} loadMore={loadMore} limit={limit}/>
            </div>
        </div>
    );
}

function PaginatedTasks({data, hasMore, loadMore, limit}) {
    return (
        <div>
            {hasMore && data.length >= limit && (
                <div className="add-task-card" onClick={loadMore}>
                    <span className="add-task-icon">&gt;</span>
                </div>
            )}
        </div>)
}

function CreateTask() {
    const [name, setName] = useState('');
    const [error, setError] = useState(false);
    const param = useParams()
    const auth = useContext(AuthContext)
    const navigate = useNavigate();

    const userID = auth.userID
    const robotID = param.id

    async function clickHandler() {
        const body: TaskInputModel = {name, userID, robotID};
        const uri = '/api/task';

        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            if (jsonData.robotId) {
                navigate('/tasks')
            } else {
                setError(true);
            }
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div>
            <CreateTaskForm
                name={name}
                changeHandlerName={event => setName(event.target.value)}
                clickHandler={clickHandler}
                error={error}
            />
        </div>
    );
}
