import React, {useContext} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {AuthContext} from "../App";
import {useCookies  } from 'react-cookie';
import {fetchWrapper} from "../fetch/fetchPost";
import {LoginInputModel} from "../types/userInputModel";

export function NavBar() {
    const auth = useContext(AuthContext)
    const userID = auth.userID
    return (
        <div className="nav">
            <Link className="home" to={"/user/" + userID}>HOME</Link>
            <span className="spacer"></span>
            <Link  to="/robots">Robots</Link>
            <Link to="/tasks">Tasks</Link>
            <Logout/>
        </div>
    );
}


function Logout() {
    const auth = useContext(AuthContext);
    const navigate = useNavigate();
    const [cookies,setCookie, removeCookie] = useCookies(['token']); //setCookie is not used but is needed to get removeCookie correctly

    const id = auth.userID;
    const uri = '/api/user/logout';
    const token = cookies.token;

    async function clickHandler() {
        const body:LoginInputModel = { id,token };
        try {
            const jsonData = await fetchWrapper(uri, 'POST', body);
            if (jsonData.id) {
                removeCookie('token', { path: '/' });
                auth.setUserID(null);
                navigate('/');
            } else {
                navigate('/error');
            }
        } catch (error) {
            navigate('/error');
        }
    }

    return (
        <Link to="/" onClick={clickHandler}>Logout</Link>
    );
}