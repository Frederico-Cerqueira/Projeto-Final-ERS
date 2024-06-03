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
        <label>Name:</label>
        <input type="text" className="input-styled" value={name} onChange={changeHandlerName}/>

        <br/>

        <label>Email:</label>
        <input type="text" value={email} onChange={changeHandlerEmail}/>

        <br/>

        <label>Password:</label>
        <input type="password" autoComplete="off" value={password}
               onChange={changeHandlerPassword}/>
        <Error error={error} msg={msg}/>
        <button onClick={clickHandler}>{buttonName}</button>
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