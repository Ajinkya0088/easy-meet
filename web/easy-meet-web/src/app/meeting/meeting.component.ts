import { Component, OnInit } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore'
import { AuthService } from  '../auth/auth.service';
import {OnChangeComponentService} from '../on-change-component.service';
import {Meetings} from '../meetings.model';
@Component({
  selector: 'app-meeting',
  templateUrl: './meeting.component.html',
  styleUrls: ['./meeting.component.css']
})
export class MeetingComponent implements OnInit {

   meetingArray:Array<Meetings>;
  constructor(private  authService:  AuthService, private fire : AngularFirestore,private click:OnChangeComponentService) {
    // :Array<Meetings>
    this.meetingArray=new Array();
  }
   meeting:Meetings;
  
  ngOnInit() {
    this.getMeeting();
    
  }

  // getMeetings(){
  //   this.fire.collection("users_meetings").doc("adityapingle90@gmail.com").valueChanges()
  //   .subscribe(res =>{
  //     // console.log(res);
      
  //   });

  // }
  i:number =0;
 j:number =0;

  getMeeting() {
   this.fire.collection('users_meetings').doc(JSON.parse(localStorage.getItem('user')).email)
    .get()
    .subscribe((res) =>{
      var ids=res.data() as string[];
      const len=Object.keys(ids).length;
      for(this.i=0;this.i<len;this.i++){
        // console.log(ids[this.i])
        this.fire.collection("meetings").doc(ids[this.i]).get().subscribe(result=>{
          // console.log(result.data());
          this.meeting=result.data() as Meetings; 
          var labels=res.data().speaker_labels as string[];
          if(labels!=null){
          const leng=Object.keys(labels).length;
          this.meeting.speaker_labels = [];
          for(this.j=0;this.j<leng;this.j++){
            this.meeting.speaker_labels.push(labels[this.j]);
          }
          } else {
            this.meeting.speaker_labels=[];
          }
          this.meetingArray.push(this.meeting);
        });
      }
    });
  }

  
}
