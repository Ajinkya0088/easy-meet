import { Component, OnInit } from '@angular/core';

import { AuthService } from  '../auth/auth.service';
@Component({
  selector: 'app-summery',
  templateUrl: './summery.component.html',
  styleUrls: ['./summery.component.css']
})
export class SummeryComponent implements OnInit {

  constructor(private  authService:  AuthService) { }

  ngOnInit() {
  }

}
