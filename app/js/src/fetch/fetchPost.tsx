export async function fetchPost(uri: string, body: Object): Promise<string> {
    let response = await fetch(uri, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    });
    return await response.text();
}