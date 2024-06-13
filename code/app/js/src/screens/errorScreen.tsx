import React from "react";
import {InitialNavBar} from "../elements/initialNavBar";

export function Error() {
    return (
        <div>
            <InitialNavBar/>
            <div className="error-container">
                <h2>Error Page</h2>
                <p>Please Try Later</p>
            </div>
        </div>
    );
}