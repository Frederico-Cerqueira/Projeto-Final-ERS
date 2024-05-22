import React, {useContext} from "react";
import {Link} from "react-router-dom";
import {convertToObject} from "../fetch/fetchGet";
import {AuthContext} from "../App";

export function Robots() {
    const auth = useContext(AuthContext)
    const userID = auth.userID
    const robots = convertToObject('/api/robot?offset=0&limit=100', 'robots')
    console.log(robots)

    return (
        <div>
            <h1>Robots</h1>
            <p><Link to={"/user/"+userID}>Back to User</Link></p>
            <p><Link to="/createRobot">Create Robot</Link></p>
            <div>{robots !== undefined && robots.map(robot => (
                <div key={robot.id}>
                    <h2><Link to={`/robot/`+robot.id}>{robot.name}</Link></h2>
                    <p>Status: {robot.status}</p>
                    <p>Characteristics: {robot.characteristics}</p>

                    <br></br>
                </div>
            ))}

            </div>
        </div>
    )
}