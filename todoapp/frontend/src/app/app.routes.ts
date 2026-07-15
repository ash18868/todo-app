import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Register } from './components/register/register';
import { Dashboard } from './components/dashboard/dashboard';
import { Subtask } from './components/subtask/subtask';
import { Todo } from './components/todo/todo';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  
  { 
    path: '', 
    redirectTo: 'login', 
    pathMatch: 'full' 
  },
  { 
    path: 'login', 
    component: Login 
  },
  { 
    path: 'register', 
    component: Register 
  },
  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [authGuard],
    children: [
        {
            path: 'todo',
            component: Todo,
            children: [
              {
                path: 'subtask',
                component: Subtask
              }
            ]
        }
    ]
  }
]