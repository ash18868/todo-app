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
  imports: [RouterLink, AsyncPipe, FormsModule, Subtask, Todo],
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
        this.todoService.getTodos();
      } catch {
        this.username = 'User';
      }
    }
  }

  addTodo(title: string): void {
    const trimmedTitle = title.trim();
    if (!trimmedTitle) {
      return;
    }

    const todo: Partial<TodoModel> = {
      title: trimmedTitle,
      completed: false
    };

    this.todoService.addTodo(todo).subscribe(() => {
      this.todoService.getTodos();
    });
  }

  editTodo(todo: TodoModel): void {
    this.editingTodoId = todo.todoId;
    this.editedTitle = todo.title;
  }

  saveTodo(todo: TodoModel): void {
    const updatedTodo: TodoModel = {
      ...todo,
      title: this.editedTitle
    };

    this.todoService.updateTodo(updatedTodo).subscribe(() => {
      this.editingTodoId = null;
      this.editedTitle = '';
      this.todoService.getTodos();
    });
  }

  cancelEdit(): void {
    this.editingTodoId = null;
    this.editedTitle = '';
  }

  deleteTodo(todo: TodoModel): void {
    this.todoService.deleteTodo(todo).subscribe(() => {
      this.todoService.getTodos();
    });
  }

  toggleTodoCompleted(todo: TodoModel, completed: boolean): void {
    const updatedTodo: TodoModel = {
      ...todo,
      completed: completed
    };

    this.todoService.updateTodo(updatedTodo).subscribe(() => {
      this.todoService.getTodos();
    });
  }

    logout(): void {
  this.authService.logout();
  }
}
