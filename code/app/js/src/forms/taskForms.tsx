import React, {ChangeEvent} from "react";


export function CreateTaskForm({
                                   name,
                                   changeHandlerName,
                                   clickHandler,
                               }: {
    name: string,
    changeHandlerName: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void,
}) {
    return (
        <div>
            <label>Name:</label>
            <input type="text" value={name} onChange={changeHandlerName}/>
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