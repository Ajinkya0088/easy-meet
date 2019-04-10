import { Component, OnInit } from '@angular/core';
import { AuthService } from  '../auth/auth.service';
@Component({
  selector: 'app-summerize',
  templateUrl: './summerize.component.html',
  styleUrls: ['./summerize.component.css']
})
export class SummerizeComponent implements OnInit {

  constructor(private  authService:  AuthService) { }

  ngOnInit() {
  }

}
