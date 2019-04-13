import { NgModule, Component }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent }   from './login/login.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import { MeetingComponent } from './meeting/meeting.component';
import { AdminGuard } from  './admin/admin.guard';
import {TaskComponent} from './task/task.component';
import {UserComponent} from './user/user.component';
const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AdminGuard]},
  { path:'meeting', component: MeetingComponent , canActivate: [AdminGuard]},
  {path:'task', component:TaskComponent, canActivate: [AdminGuard]},
  {path:'user', component:UserComponent, canActivate: [AdminGuard]}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}