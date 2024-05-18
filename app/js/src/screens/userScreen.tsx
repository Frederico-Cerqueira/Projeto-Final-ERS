import React, {useState} from "react";
import {Link, useParams} from "react-router-dom";
import {convertToObject} from "../fetch/fetchGet";


export function User() {
    const param = useParams()
    console.log("param = " + param.id)
    const [user, setUser] = useState(null)

    /** FAZ COM ESTES FETCHES ELES FUNCIONAM
     * **/
    fetch(`/api/user/`+ param.id).then(response => response.json()).then(data => setUser(data))

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

