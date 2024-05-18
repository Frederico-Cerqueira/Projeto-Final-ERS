import {useEffect, useState} from "react";

export async function fetchGet(uri: string) {
    let response = await fetch(uri);
    return await response.text();
}

/** ESTAS DUAS É QUE NÃO DEIXAVAM FAZER ESTA PORRA BEM
 * */
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

export function convertToObject(uri, propertyName = undefined) {
    const content = useFetchGet({uri});
    let object;
    if (propertyName == undefined) {
        if (content !== undefined) {
            object = JSON.parse(content);
            console.log("AAA" + object);
        }
    } else {
        if (content !== undefined) {
            console.log("PN" + propertyName);
            object = JSON.parse(content)[propertyName];

            console.log("CC" + object);
            console.log("BB" + content);
        }
    }

    return object;
}

