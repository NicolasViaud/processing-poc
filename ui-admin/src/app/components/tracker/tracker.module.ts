import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TrackerComponent } from './tracker.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTableModule } from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    TrackerComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    MatTableModule,
    MatProgressBarModule,
    MatPaginatorModule
  ]
})
export class TrackerModule { }
