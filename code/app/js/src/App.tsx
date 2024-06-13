import React, {createContext, useEffect, useState} from 'react';
import {createRoot} from "react-dom/client";
import {RouterProvider} from "react-router";
import {router} from "./router";
import {useCookies} from "react-cookie";

export type ContextType = {
    userID: number,
    setUserID?: (id: number) => void,
}

export const AuthContext = createContext<ContextType>({userID: 1})

function App() {
    const [userID, setUserID] = useState(null)
    const [cookies, setCookie, removeCookie] = useCookies(['token']);

    useEffect(() => {
        const fetchUserID = async () => {
            if (cookies.token) {
                try {
                    const response = await fetch('/api/user/token?token=' + cookies.token);
                    if (response.ok) {
                        const data = await response.json();
                        setUserID(data.id);
                    } else {
                        console.error('Failed to fetch user ID:', response.statusText);
                    }
                } catch (error) {
                    console.error('Failed to fetch user ID:', error);
                }
            }
        };

        fetchUserID();
    }, [cookies.token]);

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