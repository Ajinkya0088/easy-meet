import { Injectable } from '@angular/core';
import {Meetings} from './meetings.model';
@Injectable({
  providedIn: 'root'
})
export class OnChangeComponentService {

  constructor() { }
  meeting:Meetings
  componentOption:boolean = false;
  changeComponent(){
    this.componentOption = !this.componentOption;
  }
  goDetail(meeting){
    console.log(meeting)
    this.meeting=meeting as Meetings;
    this.changeComponent();         
  }
  

}
