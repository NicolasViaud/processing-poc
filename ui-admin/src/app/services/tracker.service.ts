import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TrackerPage } from '../models/tracker';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PageRequest } from '../models/page';


@Injectable({
  providedIn: 'root'
})
export class TrackerService {

  constructor(private http: HttpClient) { }

  getTrackers(pageRequest?:PageRequest): Observable<TrackerPage>{
    let params:any={
      sort:"creationDate,desc"
    };
    if(pageRequest){
      params.size=pageRequest.size;
      params.page=pageRequest.page;
    }
    return this.http.get<TrackerPage>(`${environment.url.trackerRootApi}/trackers`,{
      params:params
    });
  }
  

}
