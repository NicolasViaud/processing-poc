import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmbeddedComponent } from './embedded.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    EmbeddedComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ]
})
export class EmbeddedModule { }
