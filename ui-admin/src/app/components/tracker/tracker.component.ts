import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import {  BehaviorSubject, map, mergeMap, Observable, Subject, tap, timer } from 'rxjs';
import { Page, PageRequest } from 'src/app/models/page';
import { Tracker, TrackerPage } from 'src/app/models/tracker';
import { TrackerService } from 'src/app/services/tracker.service';

@Component({
  selector: 'app-tracker',
  templateUrl: './tracker.component.html',
  styleUrls: ['./tracker.component.scss']
})
export class TrackerComponent implements OnInit {
  displayedColumns: string[] = ['processingId', 'creationDate', 'lastRefresh', 'progress'];
  
  pageResult?:Page;
  pageSizeOptions: number[] = [1, 5, 10, 25, 100];

  pageRequest?:PageRequest;

  ticker:Subject<any>;
  datasource:Observable<Tracker[]>;
  trackers:Observable<TrackerPage>;

  constructor(private trackerService:TrackerService) {
    this.ticker = new BehaviorSubject(true)
    this.trackers = this.ticker.pipe(
      mergeMap(()=> this.trackerService.getTrackers(this.pageRequest)),
      tap(tracker=>{
        this.pageResult=tracker.page;
      })
    );
    this.datasource = this.trackers.pipe(
      map(trackers=>trackers._embedded.trackers)
    );
   }

  ngOnInit(): void {
    this.ticker.subscribe();
    this.refresh();
    setInterval(()=>this.refresh(),1000);
  }

  refresh(){
    this.ticker.next(true);    
  }

  onChangePage(event: PageEvent) {
    console.log(event);
    this.pageRequest = {
      page:event.pageIndex,
      size:event.pageSize
    }
    this.refresh();
  }

}
