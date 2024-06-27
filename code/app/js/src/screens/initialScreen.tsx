import React from 'react'
import '../../css/initialScreen.css';
import {InitialNavBar} from "../elements/initialNavBar";

export function InitialScreen() {
    return (
        <div className="container">
            <InitialNavBar/>
            <div className="content">
                <p className="big-text">BEM-VINDO AO ERS </p>
            </div>
        </div>
    );
}

