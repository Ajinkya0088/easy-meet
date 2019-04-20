import { Component, OnInit } from '@angular/core';

import { AngularFirestore } from '@angular/fire/firestore'
import { AuthService } from  '../auth/auth.service';
import{Users} from '../users.model';
import { Task } from '../task.model';
@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {

  taskArray:Array<Task>;
  constructor(private fire : AngularFirestore) {
    this.taskArray=new Array();
   }
   t:Task;
  ngOnInit() {
    this.getTask();
  }
  i:number =0;
  getTask() {
    this.fire.collection('tasks').doc(JSON.parse(localStorage.getItem('user')).email)
    .get()
    .subscribe((res) =>{
      var ids=res.data() as string[];
      const len=Object.keys(ids).length;
      for(this.i=0;this.i<len;this.i++){
        //  console.log(ids[this.i])
         this.t=ids[this.i] as any;
         this.taskArray.push(this.t);
      }
    })
   }
}
 
