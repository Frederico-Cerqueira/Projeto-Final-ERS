import React, {useContext, useState} from 'react'
import {useNavigate} from "react-router-dom";
import {fetchWrapper} from "../fetch/fetchPost";
import {UserInputModel} from "../types/userInputModel";
import {AuthContext} from "../App";
import {useCookies} from 'react-cookie';

import "../../css/authForm.css"
import '../../css/initialScreen.css';
import {AuthForm} from "../forms/AuthForm";

export function UserManagement({uri, msg, buttonName, link, linkMessage}: {
    uri: string,
    msg: string,
    buttonName: string,
    link: string,
    linkMessage: string
}) {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(false);
    const navigate = useNavigate();
    const auth = useContext(AuthContext)

    async function clickHandler() {
        const body: UserInputModel = {name, email, password}
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            if (jsonData.id) {
                auth.setUserID(jsonData.id);
                navigate('/user/' + jsonData.id)
            } else {
                setError(true);
            }
        } catch (error) {
            setError(true);
        }
    }

    return (
        <div>
            <AuthForm
                name={name}
                password={password}
                email={email}
                error={error}
                msg={msg}
                changeHandlerName={event => setName(event.target.value)}
                changeHandlerPassword={event => setPassword(event.target.value)}
                changeHandlerEmail={event => setEmail(event.target.value)}
                clickHandler={clickHandler}
                buttonName={buttonName}
                link={link}
                linkMessage={linkMessage}/>
        </div>
    );
}

