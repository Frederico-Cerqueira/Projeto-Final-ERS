import React, {useState} from "react";
import {Link, useNavigate, useParams} from "react-router-dom";
import {AreaInputModel} from "../types/areaInputModel";
import {fetchWrapper} from "../fetch/fetchPost";
import {CreateAreaForm} from "../forms/areaForms";
import {TimeInputModel} from "../types/timeInputModel";
import {CreateTimeForm} from "../forms/timeForms";
import {TaskUpdateInputModel} from "../types/taskInputModel";
import {UpdateTaskForm} from "../forms/taskForms";
import {useFetchGet} from "../fetch/fetchGet";
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
                        <UpdateTask taskID={taskID} setTask={setTask}/>
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

function UpdateTask({taskID, setTask}) {
    const [status, setStatus] = useState('');
    const [error, setError] = useState(false);

    const body: TaskUpdateInputModel = {status};
    const uri = '/api/task/update/' + taskID;

    async function clickHandler() {
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success!', jsonData)
            if (jsonData.robotId) setTask(jsonData);
            else {
                setError(true);
            }
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div>
            <UpdateTaskForm
                status={status}
                changeHandlerStatus={event => setStatus(event.target.value)}
                clickHandler={clickHandler}
                error={error}
            />
        </div>
    );
}


function GetAreas({taskID}) {
    const [areas, setAreas] = useState(null)
    const [showForm, setShowForm] = useState(false);

    const uri = '/api/area/task/' + taskID + '?offset=0&limit=100';
    const toggleForm = () => setShowForm(!showForm);

    useFetchGet(uri, setAreas);

    return <div className="robot-grid">
        {areas && areas.areas.map(area => (
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
        ))}
        {!showForm && (
            <div className="add-robot-card" onClick={toggleForm}>
                <span className="add-robot-icon">+</span>
            </div>
        )}

        {showForm && (
            <CreateArea taskID={taskID}></CreateArea>
        )}
    </div>
}

function GetTimes({taskID}) {
    const [showForm, setShowForm] = useState(false);
    const [times, setTimes] = useState(null)

    const uri = '/api/time/task/' + taskID + '?offset=0&limit=100';
    const toggleForm = () => setShowForm(!showForm);

    useFetchGet(uri, setTimes);

    return <div className="robot-grid">
        {times && times.times.map(time => (
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
        ))}
        {!showForm && (
            <div className="add-robot-card" onClick={toggleForm}>
                <span className="add-robot-icon">+</span>
            </div>
        )}
        {showForm && (
            <CreateTime taskID={taskID}></CreateTime>
        )}
    </div>

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




