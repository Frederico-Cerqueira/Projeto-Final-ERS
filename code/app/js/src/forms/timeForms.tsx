import React, {ChangeEvent} from "react";

export function CreateTimeForm({
                                   startTime,
                                   endTime,
                                   weekDay,
                                   description,
                                   changeHandlerStartTime,
                                   changeHandlerEndTime,
                                   changeHandlerWeekDay,
                                   changeHandlerDescription,
                                   clickHandler,

                               }: {
    startTime: string,
    endTime: string,
    weekDay: string,
    description: string,
    changeHandlerStartTime: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerEndTime: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerWeekDay: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerDescription: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void,
}) {
    return (
        <div>
            <label>Start Time:</label>
            <input type="text" placeholder="Format 00:00.00" value={startTime} onChange={changeHandlerStartTime}/>

            <br/>

            <label>End Time:</label>
            <input type="text" placeholder="Format 00:00.00" value={endTime} onChange={changeHandlerEndTime}/>

            <br/>

            <label>Weekday:</label>
            <input type="text" value={weekDay} onChange={changeHandlerWeekDay}/>

            <br/>

            <label>Description:</label>
            <input type="text" value={description} onChange={changeHandlerDescription}/>

            <br/>

            <button onClick={clickHandler}>{"Create Time"}</button>
        </div>
    );
}

export function UpdateTimeForm({
                                   startTime,
                                   endTime,
                                   weekDay,
                                   changeHandlerStartTime,
                                   changeHandlerEndTime,
                                   changeHandlerWeekDay,
                                   clickHandler
                               }: {
    startTime: string,
    endTime: string,
    weekDay: string,
    changeHandlerStartTime: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerEndTime: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerWeekDay: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void
}) {
    return (
        <div>
            <label>Start Time:</label>
            <input type="text" value={startTime} onChange={changeHandlerStartTime}/>
            <br/>
            <label>End Time:</label>
            <input type="text" value={endTime} onChange={changeHandlerEndTime}/>
            <br/>
            <label>Weekday:</label>
            <input type="text" value={weekDay} onChange={changeHandlerWeekDay}/>
            <br/>
            <button onClick={clickHandler}>Update Time</button>
        </div>
    );
}
