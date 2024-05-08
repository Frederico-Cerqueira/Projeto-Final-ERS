import React, {ChangeEvent, useState} from "react";
import {Link} from "react-router-dom";
import {AreaInputModel} from "../types/AreaInputModel";
import {fetchPost} from "../fetch/fetchPost";

export function Area() {
    return (
        <div>
            Area
            <p><Link to="/robot">Robot</Link></p>
            <CreateArea></CreateArea>
        </div>
    )
}

export function CreateArea() {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [height, setHeight] = useState(0);
    const [width, setWidth] = useState(0);
    //const navigate = useNavigate();

    function changeHandlerName(event: ChangeEvent<HTMLInputElement>) {
        setName(event.target.value);
    }

    function changeHandlerDescription(event: ChangeEvent<HTMLInputElement>) {
        setDescription(event.target.value);
    }

    function changeHandlerHeight(event: ChangeEvent<HTMLInputElement>) {
        setHeight(Number(event.target.value));
    }

    function changeHandlerWidth(event: ChangeEvent<HTMLInputElement>) {
        setWidth(Number(event.target.value));
    }

    function clickHandler() {
        const body: AreaInputModel = {name, description, height, width};
        const taskID = 1;
        const uri = 'api/'+taskID+'/area';
        fetchPost(uri, body)
            .then(it => {
                const jsonData = JSON.parse(it);
                console.log(jsonData);
                if (JSON.stringify(jsonData) === '{}') {

                } else {
                    //navigate("/area");
                }
            })
            .catch();
    }

    return (
        <div className="container">
            <CreateAreaForm
                name={name}
                description={description}
                height={height}
                width={width}
                changeHandlerName={changeHandlerName}
                changeHandlerDescription={changeHandlerDescription}
                changeHandlerHeight={changeHandlerHeight}
                changeHandlerWidth={changeHandlerWidth}
                clickHandler={clickHandler}></CreateAreaForm>
        </div>
    );
}

function CreateAreaForm({
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
            <label className="labelForm">Name:</label>
            <input type="text" className="input-styled" value={name} onChange={changeHandlerName}/>

            <br/>

            <label className="labelForm">Description:</label>
            <input type="text" className="input-styled" value={description} onChange={changeHandlerDescription}/>

            <br/>

            <label className="labelForm">Height:</label>
            <input type="number" className="input-styled" value={height} onChange={changeHandlerHeight}/>

            <br/>

            <label className="labelForm">Width:</label>
            <input type="number" className="input-styled" value={width} onChange={changeHandlerWidth}/>

            <br/>
            <button className="buttonForm" onClick={clickHandler}>{"Create Area"}</button>
        </div>
    );
}

