import {useEffect, useState} from "react";

export async function fetchGet(uri: string) {
    let response = await fetch(uri);
    return await response.text();
}

export function useFetchGet({uri}: { uri: string }): string | undefined {
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