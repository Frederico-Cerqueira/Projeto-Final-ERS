export async function fetchWrapper(uri, method, body) {
    const requestOptions = {
        method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    };

    try {
        const response = await fetch(uri, requestOptions);
        if (!response.ok) {
            return response.status
        }
        return await response.json();
    } catch (error) {
        console.error('There was an error in the request:', error);
        throw error;
    }
}

