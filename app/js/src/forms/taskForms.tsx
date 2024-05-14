import React, {ChangeEvent} from "react";


export function CreateTaskForm({
                                   name,
                                   userID,
                                   robotID,
                                   changeHandlerName,
                                   changeHandlerUserID,
                                   changeHandlerRobotID,
                                   clickHandler,
                               }: {
    name: string,
    userID: number,
    robotID: number,
    changeHandlerName: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerUserID: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerRobotID: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void,
}) {
    return (
        <div>
            <label>Name:</label>
            <input type="text" value={name} onChange={changeHandlerName}/>
            <br/>
            <label>User ID:</label>
            <input type="number" value={userID} onChange={changeHandlerUserID}/>
            <br/>
            <label>Robot ID:</label>
            <input type="number" value={robotID} onChange={changeHandlerRobotID}/>
            <br/>
            <button onClick={clickHandler}>{"Create Task"}</button>
        </div>
    );
}

export function UpdateTaskForm({
                                   status,
                                   changeHandlerStatus,
                                   clickHandler
                               }: {
    status: string,
    changeHandlerStatus: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void
}) {
    return (
        <div>
            <label className="labelForm">Status:</label>
            <input type="text" className="input-styled" value={status} onChange={changeHandlerStatus}/>
            <br/>
            <button className="buttonForm" onClick={clickHandler}>{"Update Task"}</button>
        </div>
    );
}