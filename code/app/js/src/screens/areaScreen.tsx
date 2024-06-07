import React, {useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {fetchWrapper} from "../fetch/fetchPost";
import {DeleteButton} from "../elements/deteleButton";
import {AreaUpdateInputModel} from "../types/AreaInputModel";
import {useFetchGet} from "../fetch/fetchGet";
import {NavBar} from "../elements/navBar";
import '../../css/areaScreen.css';

export function Area() {
    const {taskID, id} = useParams();
    const [area, setArea] = useState(null);
    useFetchGet(`/api/area/${id}`, id, setArea);
    const navigate = useNavigate();

    async function fetchDeleteArea() {
        const uri = '/api/area/' + id;
        try {
            const jsonData = await fetchWrapper(uri, 'DELETE', {});
            console.log('Success!', jsonData);
            navigate('/task/' + taskID);
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
                setArea(jsonData);
                console.log('Success', jsonData);
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
                    Update Area
                </button>
            </div>
        );
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
                        <UpdateArea/>
                    </div>
                    <br/>
                    <DeleteButton onClick={fetchDeleteArea} name={"Area"}/>
                </div>
            )}
        </div>
    );
}
