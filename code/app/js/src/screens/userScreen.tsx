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
                <h2 className="welcome-title">Hello, {user && user.name}!</h2>
                <p className="welcome-text">{phrase}</p>
            </div>
        </div>
    );
}


function phraseRandom() {
    const funnyPhrases = [
        "Don't worry about stepping on bottles, our robots are ready to clean!",
        "Say goodbye to city trash!",
        "Why is urban cleaning fun? Because ERS exist!",
        "Our app makes the city cleaner than your grandmother's house!",
        "Cleaning up the city has never been easier - until the ERS arrive!"
    ];
    const index = Math.floor(Math.random() * funnyPhrases.length);
    return funnyPhrases[index];
}