import React, {ChangeEvent} from "react";
import '../../css/taskScreen.css';

export function CreateTaskForm({
                                   name,
                                   changeHandlerName,
                                   clickHandler,
                                   error
                               }: {
    name: string,
    changeHandlerName: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void,
    error: boolean
}) {
    return (
        <div className="create-task-form">
            <input type="text" id="task-name" value={name} onChange={changeHandlerName} placeholder={"Name"}/>
            <button onClick={clickHandler}>Create Task</button>
            {error && <div className="error-message">{"Name cannot be empty"}</div>}
        </div>
    );
}

export function UpdateTaskForm({
                                   status,
                                   changeHandlerStatus,
                                   clickHandler,
                                   error
                               }: {
    status: string,
    changeHandlerStatus: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void
    error: boolean
}) {
    return (
        <div>
            <div className="task-action-buttons">
                <input type="text" className="input-styled" value={status} onChange={changeHandlerStatus}
                       placeholder={"'pending' or 'in progress' or 'completed'"}/>
                <br/>
                <button className="buttonForm" onClick={clickHandler}>{"Update Task"}</button>


            </div>
            {error && <div
                className="error-message">{"The status can only to 'pending', 'in progress', 'completed'"}</div>}
        </div>
    );
}