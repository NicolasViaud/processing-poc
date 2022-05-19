import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  request =  {
    "imageURL": "/bucket-processing/images/in/LeeHill.tif",
    "imageSizeX": 16000,
    "imageSizeY": 8000,
    "aoi": {
      "crs": "epsg:32613",
      "upperLeft": {
      "x": 472000,
      "y": 4436000
      },
      "upperRight": {
      "x": 476000,
      "y": 4436000
      },
      "lowerLeft": {
      "x": 472000,
      "y": 4434000
      },
      "lowerRight": {
      "x": 476000,
      "y": 4434000
      }
    }
  }
  
  constructor() { }

  ngOnInit(): void {
  }

}
