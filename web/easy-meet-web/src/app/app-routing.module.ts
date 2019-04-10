import { NgModule, Component }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent }   from './login/login.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import { MeetingComponent } from './meeting/meeting.component';
import { SummerizeComponent } from './summerize/summerize.component';
import { SummeryComponent } from './summery/summery.component';
import { AdminGuard } from  './admin/admin.guard';


const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AdminGuard]},
  { path:'meeting', component: MeetingComponent , canActivate: [AdminGuard]},
  {path:'summerize', component: SummerizeComponent ,canActivate: [AdminGuard]},
  {path:'summary', component:SummeryComponent, canActivate: [AdminGuard]}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}