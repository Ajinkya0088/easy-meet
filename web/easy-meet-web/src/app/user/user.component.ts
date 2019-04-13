import { Component, OnInit } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore'
import { AuthService } from  '../auth/auth.service';
import{Users} from '../users.model';
@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  userArray:Array<Users>;
  constructor(private fire : AngularFirestore) {
    this.userArray=new Array();
   }
   u:Users;
  user:Users=  JSON.parse(localStorage.getItem('user'));
  ngOnInit() {
    this.getUser();
  }
  i:number =0;
  getUser() {
   this.fire.collection('users').get().subscribe(res=>{
     res.docs.forEach(e=>{
       console.log(e.data());
       var u=e.data() as Users;
       console.log(u.email)
       this.fire.collection('users_meetings').doc(u.email).get().subscribe(res=>{
        u.total_meeting = Object.keys(res.data() as string[]).length
        this.userArray.push(u);
      })
       
     })
   })
   }
}
