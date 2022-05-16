
export interface PageResponse<T, relation extends string > {
    _embedded: {[rel in relation]:T[]};
    _links: Links;
    page: Page;
}

export interface PageRequest {
    size: number;
    page: number;
}


export interface Page {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
}

export interface Links{
    [rel: string]:{href:string}
}

