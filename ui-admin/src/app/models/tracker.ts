import { PageResponse } from "./page";


export interface Tracker {

    processingId: string;
    progress: number;
    lastRefresh: Date;
    creationDate: Date;

}


export interface TrackerPage extends PageResponse<Tracker, "trackers">{

}
