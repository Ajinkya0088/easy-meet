import { Injectable } from '@angular/core';
import { Router } from  "@angular/router";
import { auth } from  'firebase/app';
import { AngularFireAuth } from  "@angular/fire/auth";
import { User } from  'firebase';
import { LoginComponent } from '../login/login.component';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user:  User;
  constructor(public  afAuth:  AngularFireAuth, public  router:  Router) {
    this.afAuth.authState.subscribe(user => {
      if (user) {
        this.user = user;
        localStorage.setItem('user', JSON.stringify(this.user));
      } else {
        localStorage.setItem('user', null);
      }
    })
   }
   email=""
   password=""
   async  login() {
     LoginComponent.user_email = this.email;
    try {
        await  this.afAuth.auth.signInWithEmailAndPassword(this.email, this.password)
        this.router.navigate(['/dashboard']);
    } catch (e) {
        alert("Error!"  +  e.message);
    }
    }

  async logout(){
      await this.afAuth.auth.signOut();
      localStorage.removeItem('user');
      this.router.navigate(['/login']);
  }
  get isLoggedIn(): boolean {
    const  user  =  JSON.parse(localStorage.getItem('user'));
    return  user  !==  null;
  }
}
 