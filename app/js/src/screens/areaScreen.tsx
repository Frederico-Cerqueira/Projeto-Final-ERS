import React, {useState} from "react";
import {Link, useNavigate, useParams} from "react-router-dom";

import {fetchWrapper} from "../fetch/fetchPost";
import {UpdateAreaForm} from "../forms/areaForms";
import {DeleteButton} from "../elements/deteleButton";
import {AreaUpdateInputModel} from "../types/AreaInputModel";
import {convertToObject, useFetchGet} from "../fetch/fetchGet";


export function Area() {
    const {taskID, id} = useParams()
    console.log(taskID, id)
    const [area, setArea] = useState(null)
    useFetchGet(`/api/area/${id}`, id, setArea);
    const navigate = useNavigate();
    async function fetchDeleteArea() {
        const uri = '/api/area/' + id;
        try {
            const jsonData = await fetchWrapper(uri, 'DELETE', {});
            console.log('Success!', jsonData);
            navigate('/task/' + taskID)
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    function UpdateArea() {
        const [height, setHeight] = useState(0);
        const [width, setWidth] = useState(0);

        async function clickHandler() {
            const body: AreaUpdateInputModel = {height, width};
            const uri = '/api/area/update/' + id;
            try {
                const jsonData = await fetchWrapper(uri, 'POST', body);
                setArea(jsonData)
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

    return (
        <div>
            <p><Link to={'/task/' + taskID}>Back to Task</Link></p>
            {area && (
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









