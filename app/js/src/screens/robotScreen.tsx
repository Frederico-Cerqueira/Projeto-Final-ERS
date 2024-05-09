import React, {useState} from 'react';
import {Link} from "react-router-dom";
import {fetchWrapper} from "../fetch/fetchPost";
import {RobotUpdateInputModel} from "../types/RobotInputModel";
import {UpdateRobotForm} from "../forms/robotForms";
import {DeleteButton} from "../elements/deteleButton";

export function Robot() {
    return (
        <div>
            <h1>Robot</h1>
            <p><Link to="/robots">Back to Robots</Link></p>
            <p><Link to="/tasks">My Tasks</Link></p>
            <DeleteButton onClick={fetchDeleteRobot} name={"Robot"}></DeleteButton>
            <UpdateRobotPage></UpdateRobotPage>
        </div>
    )
}

async function fetchDeleteRobot() {
    const uri = 'api/robot/1';
    try {
        const jsonData = await fetchWrapper(uri, 'DELETE', {});
        console.log('Success!', jsonData);
    } catch (error) {
        console.error('There was an error in the request:', error);
    }
}


export function UpdateRobotPage() {
    const [status, setStatus] = useState('');

    async function clickHandler() {
        const body: RobotUpdateInputModel = {status};
        const uri = 'api/robot/update/1';
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Sucesso!', jsonData);
        } catch (error) {
            console.error('Houve um erro na requisição:', error);
        }
    }
    return (
        <div className="container">
            <UpdateRobotForm
                status={status}
                changeHandlerStatus={event => setStatus(event.target.value)}
                clickHandler={clickHandler}
            />
        </div>
    );
}