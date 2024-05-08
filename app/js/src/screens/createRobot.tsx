import React, {ChangeEvent, useState} from "react";
import {Link} from "react-router-dom";
import {RobotInputModel} from "../types/RobotInputModel";
import {fetchPost} from "../fetch/fetchPost";

export function CreateRobot() {
    return (
        <div>
            Create Robot
            <p><Link to="/robot">Robot</Link></p>
            <CreateRobotPage></CreateRobotPage>
        </div>
    )
}

export function CreateRobotPage() {
    const [name, setName] = useState('');
    const [characteristics, setCharacteristics] = useState('');

    //const navigate = useNavigate();

    function changeHandlerName(event: ChangeEvent<HTMLInputElement>) {
        setName(event.target.value);
    }

    function changeHandlerCharacteristics(event: ChangeEvent<HTMLInputElement>) {
        setCharacteristics(event.target.value);
    }

    function clickHandler() {
        const body: RobotInputModel = {name, characteristics};
        const uri = 'api/robot';
        fetchPost(uri, body)
            .then(it => {
                const jsonData = JSON.parse(it);
                console.log(jsonData);
                if (JSON.stringify(jsonData) === '{}') {
                } else {
                    //navigate("/robot");
                }
            })
            .catch();
    }

    return (
        <div className="container">
            <CreateRobotForm
                name={name}
                characteristics={characteristics}

                changeHandlerName={changeHandlerName}
                changeHandlerCharacteristics={changeHandlerCharacteristics}
                clickHandler={clickHandler}
            />

        </div>
    );
}

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
            <label className="labelForm">Name:</label>
            <input type="text" className="input-styled" value={name} onChange={changeHandlerName}/>

            <br/>

            <label className="labelForm">Characteristics:</label>
            <input type="text" className="input-styled" value={characteristics}
                   onChange={changeHandlerCharacteristics}/>
            <br/>

            <button className="buttonForm" onClick={clickHandler}>{"Create Robot"}</button>
        </div>
    );
}

