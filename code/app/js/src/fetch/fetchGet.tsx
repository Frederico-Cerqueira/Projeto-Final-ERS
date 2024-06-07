import {useEffect, useState} from "react";

export async function fetchGet(uri: string) {
    let response = await fetch(uri);
    return await response.text();
}

export function useFetchGet1({uri}: { uri: string }): string | undefined {
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
    return content
}


export function convertToObject(uri, propertyName = undefined) {
    const content = useFetchGet1({uri});
    let object;
    if (propertyName == undefined) {
        if (content !== undefined) {
            object = JSON.parse(content);
        }
    } else {
        if (content !== undefined) {
            object = JSON.parse(content)[propertyName];
        }
    }

    return object;
}

export function useFetchGet(uri, id, setFunction) {
    useEffect(() => {
        fetch(uri)
            .then(response => response.json())
            .then(data => {
                console.log(data)
                setFunction(data)
            })
            .catch(error => console.error('Error:', error));
    }, [id]);
}

