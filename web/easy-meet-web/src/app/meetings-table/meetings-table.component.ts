import { Component, OnInit } from '@angular/core';

import {OnChangeComponentService} from '../on-change-component.service';

import {MeetingComponent} from '../meeting/meeting.component';

import {Meetings} from '../meetings.model';

@Component({
  selector: 'app-meetings-table',
  templateUrl: './meetings-table.component.html',
  styleUrls: ['./meetings-table.component.css']
})
export class MeetingsTableComponent implements OnInit {

  constructor(private click:OnChangeComponentService,private m:MeetingComponent) { 

  }
  ngOnInit() {
  }
  

}
