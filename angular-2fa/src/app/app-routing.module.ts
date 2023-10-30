import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [{
    path: 'login',
    component: LoginComponent
  },{
    path: 'register',
    component: RegisterComponent
  },{
    path: 'welcome',
    component: WelcomeComponent,
    canActivate: [authGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
