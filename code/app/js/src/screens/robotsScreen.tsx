import React, {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {useFetchGetToLists} from "../fetch/fetchGet";
import {NavBar} from "../elements/navBar";
import {CreateRobotForm} from "../forms/robotForms";
import {RobotInputModel} from "../types/RobotInputModel";
import {fetchWrapper} from "../fetch/fetchPost";
import '../../css/robotsScreen.css';

export function Robots() {
    const [name, setName] = useState('');
    const [characteristics, setCharacteristics] = useState('');

    const uri = '/api/robot?offset=0&limit=100';
    const robots = useFetchGetToLists(uri, 'robots')

    return (
        <div>
            <NavBar/>
            <h1 className="robots-title">Robots</h1>
            <div className="robot-grid">
                {robots !== undefined &&
                    robots.map((robot) => (
                        <div key={robot.id} className="robot-card">
                            <h2 className="robot-name">
                                <Link to={`/robot/` + robot.id} className="robot-link"> {robot.name} </Link>
                            </h2>
                            <p className="robot-status">Status: {robot.status}</p>
                            <p className="robot-characteristics">Characteristics: {robot.characteristics} </p>
                        </div>
                    ))}
                <CreateRobot name={name}
                             characteristics={characteristics}
                             setName={setName}
                             setCharacteristics={setCharacteristics}/>
            </div>
        </div>

    )
}

function CreateRobot({
                         name,
                         characteristics,
                         setName,
                         setCharacteristics
                     }) {

    const [error, setError] = useState(false);
    const [showForm, setShowForm] = useState(false);
    const navigate = useNavigate();

    const toggleForm = () => setShowForm(!showForm);

    async function clickHandler() {
        const body: RobotInputModel = {name, characteristics};
        const uri = '/api/robot';
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            if (jsonData.id) {
                navigate('/robot/' + jsonData.id)
            } else {
                setError(true);
            }
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }

    return (<div>
        {!showForm && (
            <div className="add-robot-card" onClick={toggleForm}>
                <span className="add-robot-icon">+</span>
            </div>
        )}
        {
            showForm && <CreateRobotForm
                name={name}
                characteristics={characteristics}
                changeHandlerName={event => setName(event.target.value)}
                changeHandlerCharacteristics={event => setCharacteristics(event.target.value)}
                clickHandler={clickHandler}
                error={error}
            />
        }
    </div>)
}
