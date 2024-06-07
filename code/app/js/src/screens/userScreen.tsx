import React, {useContext, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {AuthContext} from "../App";
import {NavBar} from "../elements/navBar";
import '../../css/userScreen.css';

function phraseRandom() {
    const funnyPhrases = [
        "Não se preocupe em pisar as garrafas, os nossos robôs estão prontos para limpar!",
        "Diga adeus ao lixo da cidade!",
        "Porque a limpeza urbana é divertida? Porque existem os ERS!",
        "A nossa aplicação torna a cidade mais limpa do que a casa da sua avó!",
        "Limpar a cidade nunca foi tão fácil - até chegarem os ERS!",
    ];
    const index = Math.floor(Math.random() * funnyPhrases.length);
    return funnyPhrases[index];
}

export function User() {
    const auth = useContext(AuthContext);
    const param = useParams();
    const [user, setUser] = useState(null);
    const [phrase, setPhrase] = useState(phraseRandom());

    useEffect(() => {
        fetch(`/api/user/${param.id}`)
            .then(response => response.json())
            .then(data => {
                console.log(data)
                setUser(data)
            })
            .catch(error => console.error('Error:', error))
    }, []);

    useEffect(() => {
        const timerId = setInterval(() => {
            setPhrase(phraseRandom());
        }, 3000);
        return () => clearInterval(timerId);
    }, []);

    return (
        <div>
            <NavBar/>
            <div className="user-container">
                <h2 className="welcome-title">Olá, {user && user.name}!</h2>
                <p className="welcome-text">{phrase}</p>
            </div>
        </div>

    );
}
