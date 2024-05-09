import React from 'react';
import {createRoot} from "react-dom/client";
import {RouterProvider} from "react-router";
import {router} from "./router";


function App() {
    return (
        <div>
            <RouterProvider router={router}/>
        </div>
    )

}

export function app() {
    const root = createRoot(document.getElementById("container"))
    root.render(<App/>)
}