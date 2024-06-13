import React, {ChangeEvent} from "react";

export function CreateRobotForm({
                                    name,
                                    characteristics,
                                    changeHandlerName,
                                    changeHandlerCharacteristics,
                                    clickHandler,
                                    error

                                }: {
    name: string,
    characteristics: string,
    changeHandlerName: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerCharacteristics: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void,
    error: boolean

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
            {error && <div className="error-message">{"Name or Characteristics cannot be empty"}</div>}
        </div>
    )
}
