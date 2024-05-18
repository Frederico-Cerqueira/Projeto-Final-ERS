import React, {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {useFetchGet} from "../fetch/fetchGet";



export function User() {
    const param = useParams()
    const [user, setUser] = useState(null)
    useFetchGet(`/api/user/${param.id}`, param.id, setUser);

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

