import React from "react";

export function Form({
                         name,
                         password,
                         email,
                         error,
                         msg,
                         changeHandlerName,
                         changeHandlerPassword,
                         changeHandlerEmail,
                         clickHandler,
                         buttonName
                     }) {
    return (<div>
        <label className="labelForm">Name:</label>
        <input type="text" className="input-styled" value={name} onChange={changeHandlerName}/>

        <br/>

        <label className="labelForm">Email:</label>
        <input type="text" className="input-styled" value={email} onChange={changeHandlerEmail}/>

        <br/>

        <label className="labelForm">Password:</label>
        <input type="password" className="input-styled" autoComplete="off" value={password}
               onChange={changeHandlerPassword}/>
        <Error error={error} msg={msg}/>
        <button className="buttonForm" onClick={clickHandler}>{buttonName}</button>
    </div>)
}

function Error({error, msg}: { error: boolean, msg: string }) {
    if (error) {
        return (
            <div className="error">
                {msg}
            </div>
        )
    } else {
        return (
            <div>
            </div>
        )
    }
}