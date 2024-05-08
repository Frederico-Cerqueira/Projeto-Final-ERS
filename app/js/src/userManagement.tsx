import React, {ChangeEvent, useState} from 'react'
import {Link, useNavigate} from "react-router-dom";
import {Form} from "./elements/form";
import {fetchPost} from "./fetch/fetchPost";
import {UserInputModel} from "./types/UserInputModel";

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

    function changeHandlerName(event: ChangeEvent<HTMLInputElement>) {
        setName(event.target.value)
    }

    function changeHandlerEmail(event: ChangeEvent<HTMLInputElement>) {
        setEmail(event.target.value)
    }
    function changeHandlerPassword(event: ChangeEvent<HTMLInputElement>) {
        setPassword(event.target.value)
    }

    function clickHandler() {
        const body: UserInputModel = {name: name,email:email,password: password}
        fetchPost(uri, body)
            .then(it => {
                const jsonData = JSON.parse(it);
                console.log(jsonData);
                if(JSON.stringify(jsonData) === '{}'){                                    //CORRIGIR ISTO
                    setError(true);
                }
                else{
                    navigate("/user");
                }
            })
            .catch(//() =>
                //navigate("/error")
            );
    }


    return (
        <div className="container">
            <Form
                name={name}
                password={password}
                email={email}
                error={error}
                msg={msg}
                changeHandlerName={changeHandlerName}
                changeHandlerPassword={changeHandlerPassword}
                changeHandlerEmail={changeHandlerEmail}
                clickHandler={clickHandler}
                buttonName={buttonName}/>
            <Link to={link}>{linkMessage}</Link>
        </div>
    );
}

