import React, {useState} from "react";
import {Link} from "react-router-dom";

import {fetchWrapper} from "../fetch/fetchPost";
import {UpdateAreaForm} from "../forms/areaForms";
import {DeleteButton} from "../elements/deteleButton";
import {AreaUpdateInputModel} from "../types/AreaInputModel";
import {convertToObject} from "../fetch/fetchGet";


export function Area() {
    const area = convertToObject(`api/area/1`)

    return (
        <div>
            <p><Link to="/task">Back to Task</Link></p>
            {area !== undefined && (
                <div>
                    <h1>{area.name}</h1>
                    <p>Size: {area.width}x{area.height}</p>
                    <p>Description: {area.description}</p>
                    <br></br>
                </div>
            )}
            <UpdateArea></UpdateArea>
            <DeleteButton onClick={fetchDeleteArea} name={"Area"}></DeleteButton>
        </div>
    )
}


async function fetchDeleteArea() {
    const uri = 'api/area/1';
    try {
        const jsonData = await fetchWrapper(uri, 'DELETE', {});
        console.log('Success!', jsonData);
    } catch (error) {
        console.error('There was an error in the request:', error);
    }
}


export function UpdateArea() {
    const [height, setHeight] = useState(0);
    const [width, setWidth] = useState(0);

    async function clickHandler() {
        const body: AreaUpdateInputModel = {height, width};
        const uri = 'api/area/update/1';
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success', jsonData);
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div>
            <UpdateAreaForm
                height={height}
                width={width}
                changeHandlerHeight={event => setHeight(Number(event.target.value))}
                changeHandlerWidth={event => setWidth(Number(event.target.value))}
                clickHandler={clickHandler}
            />
        </div>
    );
}



