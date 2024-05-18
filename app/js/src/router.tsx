import React from 'react'
import {createBrowserRouter} from "react-router-dom";
import {InitialScreen} from "./screens/initialScreen";
import {CreateUser} from "./screens/createUserScreen";
import {Login} from "./screens/loginScreen";
import {User} from "./screens/userScreen";
import {Robots} from "./screens/robotsScreen";
import {Tasks} from "./screens/tasksScreen";
import {Area} from "./screens/areaScreen";
import {CreateRobot} from "./screens/createRobotScreen";
import {Robot} from "./screens/robotScreen";
import {Task} from "./screens/taskScreen";
import {Time} from "./screens/timeScreen";
import {CreateTask} from "./screens/createTaskScreen";


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
                    path: "/area/:id",
                    element: <Area/>
                },
                {
                    path: "/createRobot",
                    element: <CreateRobot/>
                },
                {
                    path: "/createUser",
                    element: <CreateUser/>,
                },
                {
                    path: "/createTask",
                    element: <CreateTask/>,
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
                    path: "/time/:id",
                    element: <Time/>
                },
                {
                    path: "/user/:id",
                    element: <User/>
                },

            ]
        }
    ]
)


