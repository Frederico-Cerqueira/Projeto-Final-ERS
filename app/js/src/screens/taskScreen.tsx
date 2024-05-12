import React, {useState} from "react";
import {Link} from "react-router-dom";
import {AreaInputModel} from "../types/AreaInputModel";
import {fetchWrapper} from "../fetch/fetchPost";
import {CreateAreaForm} from "../forms/areaForms";
import {TimeInputModel} from "../types/TimeInputModel";
import {CreateTimeForm} from "../forms/timeForms";
import {TaskUpdateInputModel} from "../types/TaskInputModel";
import {UpdateTaskForm} from "../forms/taskForms";
import {DeleteButton} from "../elements/deteleButton";
import {convertToObject} from "../fetch/fetchGet";

export function Task() {

    return (
        <div>
            <h1>Task</h1>
            <p><Link to="/robot">Robot</Link></p>
            <div>
                <h2>Create Area</h2>
                <CreateArea></CreateArea>
            </div>
            <div>
                <h2>Create Time</h2>
                <CreateTime></CreateTime>
            </div>
            <br></br>
            <DeleteButton onClick={fetchDeleteTask} name={"Task"}></DeleteButton>
            <br></br>
            <UpdateTask></UpdateTask>
            <GetAreas></GetAreas>
            <GetTimes></GetTimes>
        </div>
    )
}

function GetAreas() {
    const areas = convertToObject(`api/area/task/1?offset=0&limit=100`, 'areas');
    return <div>
        <h1>Areas</h1>
        {areas && areas.map(area => (
            <div key={area.id}>
                <h2><Link to={"/area"}>{area.name}</Link></h2>
                <p>Description: {area.description}</p>
                <p>Size: {area.width}x{area.height}</p>
            </div>
        ))}
    </div>
}

function GetTimes() {
    const times = convertToObject(`api/time/task/1?offset=0&limit=100`, 'times');
    return <div>
        <h1>Times</h1>
        {times && times.map(time => (
            <div key={time.id}>
                <h2><Link to={"/time"}>{time.description}</Link></h2>
                <h2>{time.weekDay}</h2>
                <p>Start Time: {time.startTime}</p>
                <p>End Time: {time.endTime}</p>
            </div>
        ))}
    </div>
}

function CreateArea() {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [height, setHeight] = useState(0);
    const [width, setWidth] = useState(0);

    //const navigate = useNavigate();

    async function clickHandler() {
        const body: AreaInputModel = {name, description, height, width};
        const taskID = 1;
        const uri = 'api/area/+' + taskID;

        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success!', jsonData);
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
                clickHandler={clickHandler}></CreateAreaForm>
        </div>
    );
}


function CreateTime() {
    const [startTime, setStartTime] = useState('');
    const [endTime, setEndTime] = useState('');
    const [weekDay, setWeekDay] = useState('');
    const [description, setDescription] = useState('');

    async function clickHandler() {
        const body: TimeInputModel = {startTime, endTime, weekDay, description};
        const taskID = 1;
        const uri = 'api/time/' + taskID;
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success!', jsonData);
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
                clickHandler={clickHandler}/>
        </div>
    );
}

async function fetchDeleteTask() {
    const uri = 'api/task/1';
    try {
        const jsonData = await fetchWrapper(uri, 'DELETE', {});
        console.log('Success!', jsonData);
    } catch (error) {
        console.error('There was an error in the request:', error);
    }
}


function UpdateTask() {
    const [status, setStatus] = useState('');

    async function clickHandler() {
        const body: TaskUpdateInputModel = {status};
        const uri = 'api/task/update/1';
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success!', jsonData);
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
            />
        </div>
    );
}

