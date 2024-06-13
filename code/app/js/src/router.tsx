import React from 'react'
import {createBrowserRouter, Link} from "react-router-dom";
import {InitialScreen} from "./screens/initialScreen";
import {CreateUser} from "./screens/createUserScreen";
import {Login} from "./screens/loginScreen";
import {User} from "./screens/userScreen";
import {Robots} from "./screens/robotsScreen";
import {Tasks} from "./screens/tasksScreen";
import {Area} from "./screens/areaScreen";
import {Robot} from "./screens/robotScreen";
import {Task} from "./screens/taskScreen";
import {Time} from "./screens/timeScreen";
import {Error} from "./screens/errorScreen";

export const router = createBrowserRouter(
    [
        {
            path: "/",
            children: [
                {
                    path: "/",
                    element: <InitialScreen/>
                },
                {
                    path: "/task/:taskID/area/:id",
                    element: <Area/>
                },
                {
                    path: "/createUser",
                    element: <CreateUser/>,
                },
                {
                    path: "/login",
                    element: <Login/>
                },
                {
                    path: "/robot/:id",
                    element: <Robot/>
                },
                {
                    path: "/robots",
                    element: <Robots/>
                },
                {
                    path: "/task/:id",
                    element: <Task/>
                },
                {
                    path: "/tasks",
                    element: <Tasks/>
                },
                {
                    path: "/task/:taskID/time/:id",
                    element: <Time/>
                },
                {
                    path: "/user/:id",
                    element: <User/>
                },
                {
                    path: "/error",
                    element: <Error/>
                }

            ]
        }
    ]
)


