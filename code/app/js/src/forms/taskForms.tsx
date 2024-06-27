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
