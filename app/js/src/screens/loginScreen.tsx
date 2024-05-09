import React from 'react'
import {UserManagement} from "./userManagement";

export function Login() {
    return (
        <div>
            <UserManagement
                uri={"api/user/login"}
                msg={"Wrong username or password"}
                buttonName={"Login"}
                link={"/createUser"}
                linkMessage={"Don't have an account? Sign in!"}
            />
        </div>
    )
}