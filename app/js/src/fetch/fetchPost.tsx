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
            return response.status                                                               //Resolver melhor isto
        }
        return await response.json();
    } catch (error) {
        console.error('Houve um erro na requisição:', error);
        throw error;
    }
}

