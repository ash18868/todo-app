import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { TodoService } from '../../services/todo-service';
import { AsyncPipe } from '@angular/common';
import { TodoModel } from '../../models/todo-model';
import { FormsModule } from '@angular/forms';
import { Subtask } from "../subtask/subtask";

@Component({
  selector: 'app-todo',
  imports: [AsyncPipe, FormsModule, Subtask],
  templateUrl: './todo.html',
  styleUrl: './todo.css',
})
export class Todo implements OnInit{

  private readonly destroyRef = inject(DestroyRef);

  todoService = inject(TodoService)

  editingTodoId: number | null = null;
  editedTitle = '';

  ngOnInit(): void {
    this.refreshTodos();
  }

  private refreshTodos(): void {
    this.todoService
      .getTodos()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }

  addTodo(title: string){
    const trimmedTitle = title.trim();
    if (!trimmedTitle) { // Catches empty todo
      return;
    }

    const todo: Partial<TodoModel> = {
      title: trimmedTitle,
      completed: false
    };
    
    this.todoService
      .addTodo(todo)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshTodos());
  }

  deleteTodo(todo: TodoModel){
    this.todoService.deleteTodo(todo)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshTodos());
  }

  editTodo(todo: TodoModel) {
    this.editingTodoId = todo.todoId;
    this.editedTitle = todo.title;
  }

  saveTodo(todo: TodoModel) {
    const updatedTodo: TodoModel = {
      ...todo,
      title: this.editedTitle
    };
    this.todoService.updateTodo(updatedTodo)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.editingTodoId = null;
        this.editedTitle = '';
        this.refreshTodos();
      });
  }

  cancelEdit() {
    this.editingTodoId = null;
    this.editedTitle = '';
  }

  toggleTodoCompleted(todo: TodoModel, completed: boolean){
    const updatedTodo: TodoModel = {
      ...todo,
      completed: completed
    };

    this.todoService.updateTodo(updatedTodo)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshTodos());
  }

}
