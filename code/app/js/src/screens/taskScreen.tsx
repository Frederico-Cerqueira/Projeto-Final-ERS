import React, {useState} from "react";
import {Link, useNavigate, useParams} from "react-router-dom";
import {AreaInputModel} from "../types/areaInputModel";
import {fetchWrapper} from "../fetch/fetchPost";
import {CreateAreaForm} from "../forms/areaForms";
import {TimeInputModel} from "../types/timeInputModel";
import {CreateTimeForm} from "../forms/timeForms";
import {useFetchGet, usePaginatedFetch} from "../fetch/fetchGet";
import {NavBar} from "../elements/navBar";
import '../../css/taskScreen.css';
import '../../css/robotsScreen.css';

export function Task() {
    const [task, setTask] = useState(null)

    const param = useParams()
    const taskID = param.id

    useFetchGet(`/api/task/${taskID}`, setTask);

    const navigate = useNavigate();

    async function fetchDeleteTask() {
        const uri = '/api/task/' + taskID;
        try {
            await fetchWrapper(uri, 'DELETE', {});
            navigate('/tasks')
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div>
            <NavBar/>
            <br/>
            {task && (
                <div className="task-details">
                    <h1 className="task-name">{task.name}</h1>
                    <p className="task-info-item">Status: {task.status}</p>
                    <div>
                        <UpdateTask taskID={taskID} setTask={setTask} initialStatus={task.status}/>
                        <button className="delete-button" onClick={fetchDeleteTask}>Delete</button>
                    </div>
                </div>
            )}
            <br/>
            <h2>Areas:</h2>
            <GetAreas taskID={taskID}></GetAreas>
            <br/>
            <h2>Times:</h2>
            <GetTimes taskID={taskID}></GetTimes>
        </div>
    );
}

function UpdateTask({taskID, setTask, initialStatus}) {
    const [status, setStatus] = useState(initialStatus);
    const [error, setError] = useState(false);

    const fetchData = async (uri) => {
        try {
            const response = await fetch(uri);
            const data = await response.json();
            setTask(data);
            setStatus(data.status);
            setError(false);
        } catch (err) {
            setError(true);
        }
    };

    const clickHandler = () => {
        let uri;
        if (status === 'in progress') {
            uri = `/api/task/${taskID}/stop`;
        } else {
            uri = `/api/task/${taskID}/start`;
        }
        fetchData(uri);
    };

    return (
        <div>
            <button className="buttonForm" onClick={clickHandler}>{status === 'pending' ? 'Start' : 'Stop'}</button>
            {error && <div
                className="error-message">{"An error occurred"}</div>}
        </div>
    );
}


function GetAreas({taskID}) {
    const [showForm, setShowForm] = useState(false);
    const limit = 10;

    const toggleForm = () => setShowForm(!showForm);

    const renderArea = (area => (
        <div key={area.id} className="robot-card">
            <h2 className="robot-name">
                <Link to={'/task/' + taskID + '/area/' + area.id} className="area-link">
                    {area.name}
                </Link>
            </h2>
            <p className="robot-status">{area.description}</p>
            <p className="robot-status">Height: {area.height}</p>
            <p className="robot-status">Width: {area.width}</p>
        </div>
    ));
    const {data, hasMore, loadMore} = usePaginatedFetch('/api/area/task/' + taskID, limit, "areas");

    return <div className="robot-grid">
        {data.map((item) => renderArea(item))}
        {!showForm && (
            <div className="add-robot-card" onClick={toggleForm}>
                <span className="add-robot-icon">+</span>
            </div>
        )}

        {showForm && (
            <CreateArea taskID={taskID}></CreateArea>
        )}
        <PaginatedAreas data={data} hasMore={hasMore} loadMore={loadMore} limit={limit}/>
    </div>
}

function PaginatedAreas({data, hasMore, loadMore, limit}) {
    return (
        <div>
            {hasMore && data.length >= limit && (
                <div className="add-robot-card" onClick={loadMore}>
                    <span className="add-robot-icon">&gt;</span>
                </div>
            )}
        </div>)
}

function GetTimes({taskID}) {
    const [showForm, setShowForm] = useState(false);
    const limit = 10;


    const toggleForm = () => setShowForm(!showForm);

    const renderTime = (time => (
        <div key={time.id} className="robot-card">
            <h2 className="robot-name">
                <Link to={'/task/' + taskID + '/time/' + time.id} className="area-link">
                    {time.description}
                </Link>
            </h2>
            <p className="robot-status">Week Day: {time.weekDay}</p>
            <p className="robot-status">Start Time: {time.startTime}</p>
            <p className="robot-status">End Time: {time.endTime}</p>
        </div>
    ));
    const {data, hasMore, loadMore} = usePaginatedFetch('/api/time/task/' + taskID, limit, "times");

    return <div className="robot-grid">
        {data.map((item) => renderTime(item))}
        {!showForm && (
            <div className="add-robot-card" onClick={toggleForm}>
                <span className="add-robot-icon">+</span>
            </div>
        )}
        {showForm && (
            <CreateTime taskID={taskID}></CreateTime>
        )}
        <PaginatedTimes data={data} hasMore={hasMore} loadMore={loadMore} limit={limit}/>
    </div>
}

function PaginatedTimes({data, hasMore, loadMore, limit}) {
    return (
        <div>
            {hasMore && data.length >= limit && (
                <div className="add-robot-card" onClick={loadMore}>
                    <span className="add-robot-icon">&gt;</span>
                </div>
            )}
        </div>)
}

function CreateArea({taskID}) {
    const [error, setError] = useState(false);
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [height, setHeight] = useState(0);
    const [width, setWidth] = useState(0);
    const navigate = useNavigate();

    const body: AreaInputModel = {name, description, height, width};
    const uri = '/api/area/' + taskID;

    async function clickHandler() {

        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            if (jsonData.taskId) {
                navigate('/task/' + taskID + '/area/' + jsonData.id)
            } else {
                setError(true);
            }
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div className="container">
            <CreateAreaForm
                name={name}
                description={description}
                height={height}
                width={width}
                changeHandlerName={event => setName(event.target.value)}
                changeHandlerDescription={event => setDescription(event.target.value)}
                changeHandlerHeight={event => setHeight(Number(event.target.value))}
                changeHandlerWidth={event => setWidth(Number(event.target.value))}
                clickHandler={clickHandler}
                error={error}
            />
        </div>
    );
}

function CreateTime({taskID}) {
    const [error, setError] = useState(false);
    const [startTime, setStartTime] = useState('');
    const [endTime, setEndTime] = useState('');
    const [weekDay, setWeekDay] = useState('');
    const [description, setDescription] = useState('');
    const navigate = useNavigate();

    const body: TimeInputModel = {startTime, endTime, weekDay, description};
    const uri = '/api/time/' + taskID;

    async function clickHandler() {
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            if (jsonData.taskId) {
                navigate('/task/' + taskID + '/time/' + jsonData.id)
            } else {
                setError(true);
            }
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div className="container">
            <CreateTimeForm
                startTime={startTime}
                endTime={endTime}
                weekDay={weekDay}
                description={description}
                changeHandlerStartTime={event => setStartTime(event.target.value)}
                changeHandlerEndTime={event => setEndTime(event.target.value)}
                changeHandlerWeekDay={event => setWeekDay(event.target.value)}
                changeHandlerDescription={event => setDescription(event.target.value)}
                clickHandler={clickHandler}
                error={error}
            />
        </div>
    );
}




