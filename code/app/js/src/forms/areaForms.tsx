import React, {ChangeEvent} from "react";

export function CreateAreaForm({
                                   name,
                                   description,
                                   height,
                                   width,
                                   changeHandlerName,
                                   changeHandlerDescription,
                                   changeHandlerHeight,
                                   changeHandlerWidth,
                                   clickHandler,
                                   error
                               }: {
    name: string,
    description: string,
    height: number,
    width: number,
    changeHandlerName: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerDescription: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerHeight: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerWidth: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void,
    error: boolean

}) {
    return (
        <div className="form-box">
            <input
                type="text"
                value={name}
                onChange={changeHandlerName}
                placeholder="Name"
                className="input-styled"
            />
            <input
                type="text"
                value={description}
                onChange={changeHandlerDescription}
                placeholder="Description"
                className="input-styled"
            />
            <input
                type="number"
                value={height}
                onChange={changeHandlerHeight}
                placeholder="Height"
                className="input-styled"
            />
            <input
                type="number"
                value={width}
                onChange={changeHandlerWidth}
                placeholder="Width"
                className="input-styled"
            />
            <button onClick={clickHandler} className="buttonForm">
                Create Area
            </button>
            {error && (
                <div className="error-message">
                    {"Name, description, height, and width cannot be empty"}
                </div>
            )}
        </div>
    );
}
