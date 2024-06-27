import {UserManagement} from "./userManagement";
import React from "react";

export function CreateUser() {
    return (
        <div>
            <UserManagement
                uri={"/api/user"}
                msg={"Player already exists or invalid input. Please try again."}
                buttonName={"Sign up"}
                link={"/login"}
                linkMessage={"Login"}
            />
        </div>
    )
}
