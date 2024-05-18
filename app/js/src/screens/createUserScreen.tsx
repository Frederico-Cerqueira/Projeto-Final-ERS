import {UserManagement} from "./userManagement";
import React from "react";

export function CreateUser() {
    return (
        <div>
            <UserManagement
                uri={"/api/user"} // USAR O /API
                msg={"Player already exists"}
                buttonName={"Create User"}
                link={"/login"}
                linkMessage={"Login"}
            />
        </div>
    )
}
