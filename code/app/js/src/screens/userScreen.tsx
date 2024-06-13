import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {NavBar} from "../elements/navBar";
import '../../css/userScreen.css';
import {useFetchGet} from "../fetch/fetchGet";

export function User() {
    const [user, setUser] = useState(null);
    const [phrase, setPhrase] = useState(phraseRandom());

    const param = useParams();
    const userId = param.id;
    const uri = `/api/user/${userId}`;

    useFetchGet(uri, setUser);

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


function phraseRandom() {
    const funnyPhrases = [
        "Não se preocupe em pisar garrafas, os nossos robôs estão prontos para limpar!",
        "Diga adeus ao lixo da cidade!",
        "Porque a limpeza urbana é divertida? Porque existem os ERS!",
        "A nossa aplicação torna a cidade mais limpa do que a casa da sua avó!",
        "Limpar a cidade nunca foi tão fácil - até chegarem os ERS!",
    ];
    const index = Math.floor(Math.random() * funnyPhrases.length);
    return funnyPhrases[index];
}