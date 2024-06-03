import React from "react";

export function DeleteButton({onClick, name}: { onClick: () => void, name: string }) {

    function handleClick(event: React.MouseEvent<HTMLButtonElement>) {
        event.preventDefault();
        onClick();
    }

    return (
        <button onClick={handleClick}>
            {"Delete " + name}
        </button>
    );
}