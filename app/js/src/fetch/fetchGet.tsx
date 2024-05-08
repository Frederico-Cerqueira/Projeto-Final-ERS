export async function fetchGet(uri: string) {
    let response = await fetch(uri);
    return await response.text();
}