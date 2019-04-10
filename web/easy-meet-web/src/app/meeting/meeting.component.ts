import { Component, OnInit } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore'
import { AuthService } from  '../auth/auth.service';
import { LoginComponent } from '../login/login.component';
@Component({
  selector: 'app-meeting',
  templateUrl: './meeting.component.html',
  styleUrls: ['./meeting.component.css']
})
export class MeetingComponent implements OnInit {

  constructor(private  authService:  AuthService, private fire : AngularFirestore) {

  }

  ngOnInit() {
    this.jut();
  }

  jut(){
    this.fire.collection("users").doc("adityapingle90@gmail.com").get().subscribe(result=>{
      console.log(result);
    })
  }
}
