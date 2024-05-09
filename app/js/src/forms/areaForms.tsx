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

}) {
    return (
        <div>
            <label>Name:</label>
            <input type="text" value={name} onChange={changeHandlerName}/>

            <br/>

            <label>Description:</label>
            <input type="text" value={description} onChange={changeHandlerDescription}/>

            <br/>

            <label>Height:</label>
            <input type="number" value={height} onChange={changeHandlerHeight}/>

            <br/>

            <label>Width:</label>
            <input type="number" value={width} onChange={changeHandlerWidth}/>

            <br/>
            <button onClick={clickHandler}>{"Create Area"}</button>
        </div>
    );
}

export function UpdateAreaForm({
                                   height,
                                   width,
                                   changeHandlerHeight,
                                   changeHandlerWidth,
                                   clickHandler
                               }: {
    height: number,
    width: number,
    changeHandlerHeight: (event: ChangeEvent<HTMLInputElement>) => void,
    changeHandlerWidth: (event: ChangeEvent<HTMLInputElement>) => void,
    clickHandler: () => void
}) {
    return (
        <div>
            <label>Height:</label>
            <input type="number" value={height} onChange={changeHandlerHeight}/>
            <br/>
            <label>Width:</label>
            <input type="number" value={width} onChange={changeHandlerWidth}/>
            <br/>
            <button onClick={clickHandler}>Update Area</button>
        </div>
    );
}