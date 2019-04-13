import { Component, OnInit } from '@angular/core';

import {OnChangeComponentService} from '../on-change-component.service';
import {MeetingComponent} from '../meeting/meeting.component';

import {Meetings} from '../meetings.model';
@Component({
  selector: 'app-meeting-details',
  templateUrl: './meeting-details.component.html',
  styleUrls: ['./meeting-details.component.css']
})
export class MeetingDetailsComponent implements OnInit {

  constructor(private click:OnChangeComponentService) { }
  ngOnInit() {
    console.log(this.click.meeting.speaker_labels);
    console.log(this.click.meeting.participants);
    this.getUser();
  }
  getUser(){
    
  }
}
