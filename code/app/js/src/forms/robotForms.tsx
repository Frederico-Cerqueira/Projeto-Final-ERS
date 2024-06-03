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
        <div>
            <label>Name:</label>
            <input type="text" className="input-styled" value={name} onChange={changeHandlerName}/>

            <br/>

            <label>Characteristics:</label>
            <input type="text" value={characteristics}
                   onChange={changeHandlerCharacteristics}/>
            <br/>

            <button onClick={clickHandler}>{"Create Robot"}</button>
        </div>
    );
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