import React, {useState} from 'react';
import {Link, useNavigate, useParams} from "react-router-dom";
import {fetchWrapper} from "../fetch/fetchPost";
import {RobotUpdateInputModel} from "../types/RobotInputModel";
import {UpdateRobotForm} from "../forms/robotForms";
import {DeleteButton} from "../elements/deteleButton";
import {convertToObject, useFetchGet} from "../fetch/fetchGet";

export function Robot() {
    const param = useParams()
    const navigate = useNavigate();
    const robotID = param.id
    const [robot, setRobot] = useState(null)
    useFetchGet(`/api/robot/${param.id}`, param.id, setRobot);

    async function fetchDeleteRobot() {
        const uri = '/api/robot/'+param.id;
        try {
            const jsonData = await fetchWrapper(uri, 'DELETE', {});
            navigate('/robots');
            console.log('Success!', jsonData);
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }


    function UpdateRobotPage() {
        const [status, setStatus] = useState('');

        async function clickHandler() {
            const body: RobotUpdateInputModel = {status};
            const uri = '/api/robot/update/'+param.id;
            try {
                const jsonData = await fetchWrapper(uri, 'POST', body);
                setRobot(jsonData)
                console.log('Success!', jsonData);
            } catch (error) {
                console.error('There was an error in the request:', error);
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


    return (
        <div>
            <p><Link to="/robots">Back to Robots</Link></p>
            {robot && (
                <div>
                    <h1>{robot.name}</h1>
                    <p>Status: {robot.status}</p>
                    <p>Characteristics: {robot.characteristics}</p>
                    <br></br>
                </div>)
            }
            <p><Link to="/tasks">My Tasks</Link></p>
            <p><Link to={"/robot/"+robotID+"/createTask"}>CreateTask</Link></p>
            <DeleteButton onClick={fetchDeleteRobot} name={"Robot"}></DeleteButton>
            <UpdateRobotPage></UpdateRobotPage>
        </div>
    )
}
