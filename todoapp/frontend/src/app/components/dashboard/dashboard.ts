import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AsyncPipe } from '@angular/common';
import { Subtask } from '../subtask/subtask';
import { AuthService } from '../../auth/auth.service';
import { TodoService } from '../../services/todo-service';
import { TodoModel } from '../../models/todo-model';
import { FormsModule } from '@angular/forms';
import { Todo } from "../todo/todo";

@Component({
  selector: 'app-dashboard',
  imports: [FormsModule, Todo],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  private authService = inject(AuthService);
  todoService = inject(TodoService);

  username: string = 'User';
  editingTodoId: number | null = null;
  editedTitle = '';

  ngOnInit(): void {
    const token = this.authService.token();
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.username = payload.username ?? 'User';
        const userId = Number(payload.sub);
      } catch {
        this.username = 'User';
      }
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
