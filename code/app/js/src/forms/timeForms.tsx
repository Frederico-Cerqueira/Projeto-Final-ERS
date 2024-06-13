import React, {ChangeEvent} from "react";
import '../../css/taskScreen.css';

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
                                   error

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
    error: boolean
}) {
    return (
        <div className="form-box">
            <input
                type="text"
                value={weekDay}
                onChange={changeHandlerWeekDay}
                placeholder="Week Day"
                className="input-styled"
            />
            <input
                type="text"
                value={startTime}
                onChange={changeHandlerStartTime}
                placeholder="Start Time- 00:00:00"
                className="input-styled"
            />
            <input
                type="text"
                value={endTime}
                onChange={changeHandlerEndTime}
                placeholder="End Time- 00:00:00"
                className="input-styled"
            />
            <input
                type="text"
                value={description}
                onChange={changeHandlerDescription}
                placeholder="Description"
                className="input-styled"
            />
            <button onClick={clickHandler} className="buttonForm">
                Create Time
            </button>
            {error && (
                <div className="error-message">
                    {"All inputs need to be completed"}
                </div>
            )}
        </div>
    );
}


