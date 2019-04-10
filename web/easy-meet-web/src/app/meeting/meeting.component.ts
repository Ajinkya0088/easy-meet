import { Component, OnInit } from '@angular/core';

import { AuthService } from  '../auth/auth.service';
@Component({
  selector: 'app-meeting',
  templateUrl: './meeting.component.html',
  styleUrls: ['./meeting.component.css']
})
export class MeetingComponent implements OnInit {

  constructor(private  authService:  AuthService) { }

  ngOnInit() {
  }

}
