import React, {useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {fetchWrapper} from "../fetch/fetchPost";
import {AreaUpdateInputModel} from "../types/AreaInputModel";
import {useFetchGet} from "../fetch/fetchGet";
import {NavBar} from "../elements/navBar";
import '../../css/areaScreen.css';

export function Area() {
    const [area, setArea] = useState(null);

    const navigate = useNavigate();
    const {taskID, id} = useParams();

    useFetchGet(`/api/area/${id}`, setArea);

    async function fetchDeleteArea() {
        const uri = '/api/area/' + id;
        try {
            await fetchWrapper(uri, 'DELETE', {});
            navigate('/task/' + taskID);
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div>
            <NavBar/>
            <br/>
            {area && (
                <div className="area-details">
                    <h1 className="area-name">{area.name}</h1>
                    <div className="area-info-item">
                        <p>Size: {area.width}x{area.height}</p>
                        <p>{area.description}</p>
                    </div>
                    <div className="area-action-buttons">
                        <UpdateArea id={id} setArea={setArea}/>
                    </div>
                    <br/>
                    <button className="delete-button" onClick={fetchDeleteArea}>Delete</button>
                </div>
            )}
        </div>
    );
}

function UpdateArea({id, setArea}) {
    const [height, setHeight] = useState(0);
    const [width, setWidth] = useState(0);
    const [error, setError] = useState(false);

    const body: AreaUpdateInputModel = {height, width};
    const uri = '/api/area/update/' + id;

    async function clickHandler() {
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            if (jsonData.taskId) {
                setArea(jsonData);
            } else {
                setError(true);
            }
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div className="area-update-form">
            <div className="area-update-inputs">
                <input
                    type="number"
                    value={height}
                    onChange={event => setHeight(Number(event.target.value))}
                    placeholder="Height"
                />
                <input
                    type="number"
                    value={width}
                    onChange={event => setWidth(Number(event.target.value))}
                    placeholder="Width"
                />
            </div>
            <button className="area-submit-button" onClick={clickHandler}>
                Update
            </button>
            {error && (
                <div className="error-message">
                    {"Height and width cannot be empty"}
                </div>
            )}
        </div>
    );
}