import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-embedded',
  templateUrl: './embedded.component.html',
  styleUrls: ['./embedded.component.scss']
})
export class EmbeddedComponent implements OnInit {

  url?:SafeResourceUrl;

  urlWhitelist: {[key:string]:string} = {
    "controller": environment.url.controllerDocApi,
    "tracker": environment.url.trackerDocApi,
    "rabbitmq": environment.url.rabbitmqManagement,
    "pgadmin": environment.url.pgadmin,
    "zipkin": environment.url.zipkin,
  };

  constructor(
    private sanitizer: DomSanitizer,
    private route: ActivatedRoute,
    ) {
    
  }

  ngOnInit(): void {
    this.route.params.subscribe(params=>{
      let notSafeUrl = this.urlWhitelist[params['urlKey']];
      console.log(`show url ${notSafeUrl}`)
      if(notSafeUrl){
        this.url = this.sanitizer.bypassSecurityTrustResourceUrl(notSafeUrl);
      }
    });
    
  }

}
