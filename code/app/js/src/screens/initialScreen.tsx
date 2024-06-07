import React from 'react'
import '../../css/initialScreen.css';
import {InitialNavBar} from "../elements/initialNavBar";


export function InitialScreen() {
    return (
        <div className="container">
            <InitialNavBar/>
            <div className="content">
                <p className="big-text top-text">BEM-VINDO A APLICAÇÃO DO </p>
                <p className="big-text bottom-text">ECOPONTO ROBOZITADO
                    SUSTENTÁVEL</p>
            </div>
        </div>
    );
}

