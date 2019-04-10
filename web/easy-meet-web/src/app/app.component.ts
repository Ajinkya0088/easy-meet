import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'easy-meet-web';

  constructor(private router: Router){

  }

  public navigate(link:string){
  	this.router.navigateByUrl(link);
  }
}
