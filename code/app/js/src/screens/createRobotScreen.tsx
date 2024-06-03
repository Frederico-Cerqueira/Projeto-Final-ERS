import React, {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {fetchWrapper} from "../fetch/fetchPost";
import {CreateRobotForm} from "../forms/robotForms";
import {RobotInputModel} from "../types/RobotInputModel";


export function CreateRobot() {
    return (
        <div>
            <h1>Create Robot</h1>
            <p><Link to="/robots">Back to Robots</Link></p>
            <CreateRobotPage></CreateRobotPage>
        </div>
    )
}

export function CreateRobotPage() {
    const [name, setName] = useState('');
    const [characteristics, setCharacteristics] = useState('');
    const navigate = useNavigate();
    async function clickHandler() {
        const body: RobotInputModel = {name, characteristics};
        const uri = '/api/robot';
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success!', jsonData);
            navigate('/robots')
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (
        <div>
            <CreateRobotForm
                name={name}
                characteristics={characteristics}
                changeHandlerName={event => setName(event.target.value)}
                changeHandlerCharacteristics={event => setCharacteristics(event.target.value)}
                clickHandler={clickHandler}
            />

        </div>
    );
}



