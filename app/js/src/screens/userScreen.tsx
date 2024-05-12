import React from "react";
import {Link} from "react-router-dom";
import {convertToObject} from "../fetch/fetchGet";


export function User() {

    const user = convertToObject(`api/user/1`);
    return (
        <div>
            {user && (
                <h1>Hello {user.name}</h1>)
            }
            <p><Link to="/robots">Robots</Link></p>
            <p><Link to="/tasks">Tasks</Link></p>
        </div>
    )
}

