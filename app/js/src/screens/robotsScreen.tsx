import React from "react";
import {Link} from "react-router-dom";
import {useFetchGet} from "../fetch/fetchGet";

export function Robots() {

    const content = useFetchGet({uri: 'api/robot/1'})
    let robots = undefined

    if (content !== undefined) {
        robots = JSON.parse(content)
        console.log(robots)
    }
    return (
        <div>
            <h1>Robots</h1>
            <p><Link to="/user">Back to User</Link></p>
            <p><Link to="/robot">Robot</Link></p>
            <p><Link to="/createRobot">Create Robot</Link></p>
        </div>
    )
}