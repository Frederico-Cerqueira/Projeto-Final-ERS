import React, {useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {TimeUpdateInputModel} from "../types/timeInputModel";
import {fetchWrapper} from "../fetch/fetchPost";
import {useFetchGet} from "../fetch/fetchGet";
import {NavBar} from "../elements/navBar";
import '../../css/timeScreen.css';

export function Time() {
    const {taskID, id} = useParams();
    const [time, setTime] = useState(null);

    const navigate = useNavigate();

    useFetchGet(`/api/time/` + id, setTime);

    async function fetchDeleteTime() {
        const uri = '/api/time/' + id;
        try {
            await fetchWrapper(uri, 'DELETE', {});
            navigate('/task/' + taskID);
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    if (time) {
        console.log(time);
    }

    return (
        <div>
            <NavBar/>
            <br/>
            {time && (
                <div className="time-details">
                    <h1 className="time-description">{time.description}</h1>
                    <div className="time-info-item">
                        <p>Start Time: {time.startTime}</p>
                        <p>End Time: {time.endTime}</p>
                        <p>Week Day: {time.weekDay}</p>
                    </div>
                    <div className="time-action-buttons">
                        <UpdateTime id={id} setTime={setTime}/>
                    </div>
                    <button className="delete-button" onClick={fetchDeleteTime}>Delete</button>
                    <br/>
                </div>
            )}
        </div>
    );
}

function UpdateTime({id, setTime}) {
    const [startTime, setStartTime] = useState('');
    const [endTime, setEndTime] = useState('');
    const [weekDay, setWeekDay] = useState('');
    const [error, setError] = useState(false);

    const body: TimeUpdateInputModel = {startTime, endTime, weekDay};
    const uri = '/api/time/update/' + id;

    async function clickHandler() {
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            if (jsonData.taskId) {
                setTime(jsonData);
            } else {
                setError(true);
            }
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div className="time-update-form">
            <div className="time-update-inputs">
                <input
                    type="text"
                    value={startTime}
                    onChange={event => setStartTime(event.target.value)}
                    placeholder="Start Time (HH:MM:SS)"
                />
                <input
                    type="text"
                    value={endTime}
                    onChange={event => setEndTime(event.target.value)}
                    placeholder="End Time (HH:MM:SS)"
                />
                <input
                    type="text"
                    value={weekDay}
                    onChange={event => setWeekDay(event.target.value)}
                    placeholder="Week Day"
                />
            </div>
            <button className="time-submit-button" onClick={clickHandler}>
                Update
            </button>
            {error && (
                <div className="error-message">
                    {"All inputs need to be completed"}
                </div>
            )}
        </div>
    );
}
