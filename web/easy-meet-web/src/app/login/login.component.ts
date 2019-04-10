import { Component, OnInit } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
import { AppComponent } from '../app.component';
import { AuthService } from  '../auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  
  public static user_email:String = "";

  email= "";
  password = "";
  constructor(private mAuth: AngularFireAuth, private appComponent: AppComponent,private  authService:  AuthService) { 
    
  }
  ngOnInit() {
  }

  

}
