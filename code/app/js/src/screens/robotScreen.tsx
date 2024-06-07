import React, {useState} from 'react';
import {Link, useNavigate, useParams} from "react-router-dom";
import {fetchWrapper} from "../fetch/fetchPost";
import {RobotUpdateInputModel} from "../types/RobotInputModel";
import {DeleteButton} from "../elements/deteleButton";
import {convertToObject, useFetchGet} from "../fetch/fetchGet";
import {NavBar} from "../elements/navBar";
import '../../css/robotScreen.css';
import {CreateTask} from "./createTaskScreen";


export function Robot() {
    const param = useParams();
    const navigate = useNavigate();
    const robotID = param.id;
    const [robot, setRobot] = useState(null);
    useFetchGet(`/api/robot/${param.id}`, param.id, setRobot);

    async function fetchDeleteRobot() {
        const uri = '/api/robot/' + param.id;
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
            const uri = '/api/robot/update/' + param.id;
            try {
                const jsonData = await fetchWrapper(uri, 'POST', body);
                setRobot(jsonData);
                console.log('Success!', jsonData);
            } catch (error) {
                console.error('There was an error in the request:', error);
            }
        }

        return (
            <div className="robot-update-form">
                <input
                    type="text"
                    value={status}
                    onChange={event => setStatus(event.target.value)}
                    placeholder="Update"
                />
                <button className="robot-submit-button" onClick={clickHandler}>Submit</button>
            </div>
        );
    }

    function Tasks() {
        const tasks = convertToObject(`/api/task/robot/${robotID}?offset=0&limit=100`, "tasks");
        const [showForm, setShowForm] = useState(false);
        const toggleForm = () => setShowForm(!showForm);

        return (
            <div>

                <div className="task-grid">
                    {tasks !== undefined &&
                        tasks.map((task) => (
                            <div key={task.id} className="task-card">
                                <h2 className="task-name">
                                    <Link to={`/task/${task.id}`} className="task-link">
                                        {task.name}
                                    </Link>
                                </h2>
                                <p className="task-status">Status: {task.status}</p>
                            </div>
                        ))}
                    {!showForm && (
                        <div className="add-task-card" onClick={toggleForm}>
                            <span className="add-task-icon">+</span>
                        </div>
                    )}
                    {showForm && (
                        <div className="add-task-form">
                            <CreateTask/>
                        </div>
                    )}
                </div>
            </div>
        );
    }

    return (
        <div>
            <NavBar/>
            <br/>
            {robot && (
                <div className="robot-details">
                    <h1 className="robot-name">{robot.name}</h1>
                    <div className="robot-info">
                        <p className="robot-info-item">Status: {robot.status}</p>
                        <p className="robot-info-item">Characteristics: {robot.characteristics}</p>
                    </div>
                    <div className="robot-action-buttons">
                        <UpdateRobotPage/>
                        <DeleteButton onClick={fetchDeleteRobot} name={""}/>
                    </div>
                </div>
            )}
            <br/>
            <Tasks/>
        </div>
    )
}
