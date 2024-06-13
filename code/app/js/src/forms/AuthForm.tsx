import React from "react";
import {Link} from "react-router-dom";
import {InitialNavBar} from "../elements/initialNavBar";
import "../../css/authForm.css";

export function AuthForm({
                             name,
                             password,
                             email,
                             error,
                             msg,
                             changeHandlerName,
                             changeHandlerPassword,
                             changeHandlerEmail,
                             clickHandler,
                             buttonName,
                             link,
                             linkMessage
                         }) {
    return (
        <div>
            <InitialNavBar/>
            <div className="form-body">
                <div className="login-signup-form">
                    <input id="name" type="text" className="form-input" value={name}
                           onChange={changeHandlerName} placeholder="Name"/>
                    <br/>
                    <input id="email" type="text" className="form-input" value={email}
                           onChange={changeHandlerEmail} placeholder="Email"/>
                    <br/>
                    <input id="password" type="password" className="form-input" autoComplete="off" value={password}
                           onChange={changeHandlerPassword} placeholder="Password"/>
                    <button className="button-submit" onClick={clickHandler}>{buttonName}</button>
                    {error && <div className="error-message">{msg}</div>}
                    <Link to={link} className="auth-link">{linkMessage}</Link>
                </div>
            </div>
        </div>
    );
}


