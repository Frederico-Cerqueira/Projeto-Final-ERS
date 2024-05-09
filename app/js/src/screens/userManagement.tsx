import React, {useState} from 'react'
import {Link, useNavigate} from "react-router-dom";
import {Form} from "../forms/loginForm";
import {fetchWrapper} from "../fetch/fetchPost";
import {UserInputModel} from "../types/userInputModel";



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


    async function clickHandler() {
        const body: UserInputModel = {name, email, password}
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            console.log('Success!', jsonData);
        } catch (error) {
            console.error('There was an error in the request:', error);
        }
    }


    return (
        <div>
            <Form
                name={name}
                password={password}
                email={email}
                error={error}
                msg={msg}
                changeHandlerName={event => setName(event.target.value)}
                changeHandlerPassword={event => setPassword(event.target.value)}
                changeHandlerEmail={event => setEmail(event.target.value)}
                clickHandler={clickHandler}
                buttonName={buttonName}/>
            <Link to={link}>{linkMessage}</Link>
        </div>
    );
}

