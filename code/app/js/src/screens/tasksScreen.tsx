import {Link, useNavigate} from "react-router-dom";
import React, {useContext} from "react";
import {usePaginatedFetch} from "../fetch/fetchGet";
import {AuthContext} from "../App";
import '../../css/tasksScreen.css';
import {NavBar} from "../elements/navBar";


export function Tasks() {
    const navigate = useNavigate();
    const auth = useContext(AuthContext);
    const userID = auth.userID;
    if (!userID) {
        navigate('/login')
    }
    const limit = 10;

    const renderTask = (task) => (
        <div key={task.id} className="task-card">
            <h2 className="task-name">
                <Link to={`/task/${task.id}`} className="task-link">{task.name}</Link>
            </h2>
            <p className="task-status">Status: {task.status}</p>
        </div>
    );

    const {data, hasMore, loadMore} = usePaginatedFetch(`/api/task/user/${userID}`, limit, 'tasks');

    return (
        <div>
            <NavBar/>
            <h1 className="task-header">User Tasks</h1>
            <div className="task-grid">
                {data.map((task) => renderTask(task))}
                <PaginatedTasks data={data} hasMore={hasMore} loadMore={loadMore} limit={limit}/>
            </div>
        </div>
    );
}

function PaginatedTasks({data, hasMore, loadMore, limit}) {
    return (
        <div>
            {hasMore && data.length >= limit && (
                <div className="add-task-card" onClick={loadMore}>
                    <span className="add-task-icon">&gt;</span>
                </div>
            )}
        </div>
    );
}

