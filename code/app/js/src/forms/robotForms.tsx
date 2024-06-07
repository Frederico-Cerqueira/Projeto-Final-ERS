import React, {ChangeEvent} from "react";

export function CreateRobotForm({
                                    name,
                                    characteristics,
                                    changeHandlerName,
                                    changeHandlerCharacteristics,
                                    clickHandler,

                                }: {
    name: string,
    characteristics: string,
    changeHandlerName: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerCharacteristics: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void,

}) {

    return (
        <div className="create-robot-form">

            <input type="text" id="name" className="input-styled" value={name}
                   onChange={changeHandlerName} placeholder="Enter name..."/>
            <br/>

            <input type="text" id="characteristics" value={characteristics}
                   onChange={changeHandlerCharacteristics}
                   placeholder="Enter characteristics..."/>
            <br/>
            <button onClick={clickHandler}>Create Robot</button>
        </div>
    )
}

export function UpdateRobotForm({
                                    status,
                                    changeHandlerStatus,
                                    clickHandler,
                                }: {
    status: string,
    changeHandlerStatus: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void,
}) {
    return (
        <div>
            <label>Status:</label>
            <input type="text" value={status} onChange={changeHandlerStatus}/>
            <br/>
            <button onClick={clickHandler}>{"Update Robot"}</button>
        </div>
    );
}