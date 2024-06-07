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
        <div className="create-task-form">
            <input type="text" id="task-name" value={name} onChange={changeHandlerName} placeholder={"Name"}/>
            <button onClick={clickHandler}>Create Task</button>
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
            <input type="text" className="input-styled" value={status} onChange={changeHandlerStatus}
                   placeholder={"'pending' or 'in progress' or 'completed'"}/>
            <br/>
            <button className="buttonForm" onClick={clickHandler}>{"Update Task"}</button>
        </div>
    );
}