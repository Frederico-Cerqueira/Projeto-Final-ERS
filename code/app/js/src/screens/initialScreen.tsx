import React from 'react'
import '../../css/initialScreen.css';
import {InitialNavBar} from "../elements/initialNavBar";

export function InitialScreen() {
    return (
        <div className="container">
            <InitialNavBar/>
            <div className="content">
                <p className="big-text">WELCOME TO ERS MANAGEMENT APPLICATION </p>
            </div>
        </div>
    );
}

