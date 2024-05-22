import React, {createContext, useState} from 'react';
import {createRoot} from "react-dom/client";
import {RouterProvider} from "react-router";
import {router} from "./router";

export type ContextType = {
    userID : number,
    setUserID? : (f:number) => void,
}

export const AuthContext = createContext<ContextType>({userID: 1})

function App() {
    const [userID, setUserID] = useState(null)
    return (
        <div>
            <AuthContext.Provider value={{userID, setUserID}}>
            <RouterProvider router={router}/>
            </AuthContext.Provider>
        </div>
    )

}

export function app() {
    const root = createRoot(document.getElementById("container"))
    root.render(<App/>)
}