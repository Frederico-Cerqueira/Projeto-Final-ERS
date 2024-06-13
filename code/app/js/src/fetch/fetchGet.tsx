import {useEffect, useState} from "react";

export function useFetchGetToLists(uri, propertyName = undefined) {
    const [content, setContent] = useState<string>(undefined)

    useEffect(() => {
        let canceled = false

        async function doFetch() {
            const response = await fetch(uri)
            if (canceled) return
            const body = await response.text()
            if (canceled) return
            setContent(body)
        }

        setContent(undefined)
        doFetch().then(() => {
        })
        return () => {
            canceled = true
        }
    }, [])

    if (content !== undefined) {
        return JSON.parse(content)[propertyName];
    }
}

export function useFetchGet(uri, setFunction) {
    useEffect(() => {
        fetch(uri)
            .then(response => response.json())
            .then(data => {
                setFunction(data)
            })
            .catch(error => console.error('Error:', error));
    }, []);
}

