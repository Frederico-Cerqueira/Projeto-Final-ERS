import React, {useState} from "react";
import {Link, useLocation, useNavigate, useParams} from "react-router-dom";
import {TimeUpdateInputModel} from "../types/TimeInputModel";
import {fetchWrapper} from "../fetch/fetchPost";
import {UpdateTimeForm} from "../forms/timeForms";
import {DeleteButton} from "../elements/deteleButton";
import {useFetchGet} from "../fetch/fetchGet";

export function Time() {
    const {taskID,id} = useParams()
    const [time, setTime] = useState(null)
    useFetchGet(`/api/time/` + id, id, setTime);
    const navigate = useNavigate();
    async function fetchDeleteTime() {
        const uri = '/api/time/'+id;
        try {
            const jsonData = await fetchWrapper(uri, 'DELETE', {});
            console.log('Success!', jsonData);
            navigate('/task/'+taskID)
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    function UpdateTime(id) {
        const [startTime, setStartTime] = useState('');
        const [endTime, setEndTime] = useState('');
        const [weekDay, setWeekDay] = useState('');


        async function clickHandler() {
            const body: TimeUpdateInputModel = {startTime, endTime, weekDay};
            const uri = '/api/time/update/' + id.time;
            try {
                const jsonData = await fetchWrapper(uri, 'POST', body);
                console.log('Success!', jsonData);
                setTime(jsonData)
                //Navigate to Task
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

    return (
        <div>
            <p><Link to={"/task/"+taskID}>Back to Task</Link></p>
            {time && (
                <div>
                    <h1>Description: {time.description}</h1>
                    <p>Start Time: {time.startTime}</p>
                    <p>End Time: {time.endTime}</p>
                    <p>Week Day: {time.weekDay}</p>
                </div>
            )}
            <UpdateTime time={id}></UpdateTime>
            <DeleteButton onClick={fetchDeleteTime} name={"Time"}></DeleteButton>
        </div>
    )
}




