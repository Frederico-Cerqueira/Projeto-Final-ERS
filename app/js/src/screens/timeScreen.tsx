import React, {useState} from "react";
import {Link, useLocation} from "react-router-dom";
import {TimeUpdateInputModel} from "../types/TimeInputModel";
import {fetchWrapper} from "../fetch/fetchPost";
import {UpdateTimeForm} from "../forms/timeForms";
import {DeleteButton} from "../elements/deteleButton";
import {convertToObject} from "../fetch/fetchGet";

export function Time() {
    const location = useLocation();
    //const timeId = location.state?.timeId;
    console.log('location:', location)
    //console.log('timeId:', timeId)
    const times = convertToObject(`api/time/1`);


    return (
        <div>
            <p><Link to="/task">Back to Task</Link></p>
            {times && (
                <div>
                    <h1>Description: {times.description}</h1>
                    <p>Start Time: {times.startTime}</p>
                    <p>End Time: {times.endTime}</p>
                    <p>Week Day: {times.weekDay}</p>
                </div>
            )}
            <UpdateTime></UpdateTime>
            <DeleteButton onClick={fetchDeleteTime} name={"Time"}></DeleteButton>
        </div>
    )
}

async function fetchDeleteTime() {
    const uri = 'api/time/1';
    try {
        const jsonData = await fetchWrapper(uri, 'DELETE', {});
        console.log('Success!', jsonData);
    } catch (error) {
        console.error('There was an error in the request:', error);
    }
}


export function UpdateTime() {
    const [startTime, setStartTime] = useState('');
    const [endTime, setEndTime] = useState('');
    const [weekDay, setWeekDay] = useState('');


    async function clickHandler() {
        const body: TimeUpdateInputModel = {startTime, endTime, weekDay};
        const uri = 'api/time/update/1';
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success!', jsonData);
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div>
            <UpdateTimeForm
                startTime={startTime}
                endTime={endTime}
                weekDay={weekDay}
                changeHandlerStartTime={event => setStartTime(event.target.value)}
                changeHandlerEndTime={event => setEndTime(event.target.value)}
                changeHandlerWeekDay={event => setWeekDay(event.target.value)}
                clickHandler={clickHandler}
            />
        </div>
    );
}