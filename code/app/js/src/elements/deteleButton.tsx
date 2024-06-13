import React from "react";
import '../../css/robotScreen.css';

export function DeleteButton({onClick}: { onClick: () => void }) {

    function handleClick(event: React.MouseEvent<HTMLButtonElement>) {
        event.preventDefault();
        onClick();
    }

    return (
        <button className={"delete-button"} onClick={handleClick}>
            {"Delete"}
        </button>
    );
}
