import {useEffect, useState} from "react";

export function usePaginatedFetch(uriBase, limit, propertyName) {
    const [data, setData] = useState([]);
    const [offset, setOffset] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    const fetchData = (offset) => {
        const uri = `${uriBase}?offset=${offset}&limit=${limit}`;
        fetch(uri)
            .then(response => response.json())
            .then(result => {
                if (result[propertyName].length < limit) {
                    setHasMore(false);
                }
                setData(prevData => [...prevData, ...result[propertyName]]);
            });
    };

    useEffect(() => {
        fetchData(offset);
    }, [offset]);

    const loadMore = () => {
        setOffset(prevOffset => prevOffset + limit);
    };

    return { data, hasMore, loadMore };
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

