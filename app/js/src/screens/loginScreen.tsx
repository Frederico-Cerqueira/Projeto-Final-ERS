import React from 'react'
import {UserManagement} from "../userManagement";

export function Login() {
   /* const navigate = useNavigate()

    fetchGet("/api/getAuthCookie")
        .then(text => {
                if (text != undefined) {
                    const jsonData = JSON.parse(text)
                    if (jsonData.properties != undefined) {
                        if (jsonData.properties.present) {
                            navigate("/")
                        }
                    }
                }
            }
        )*/
    return (
        <div >
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