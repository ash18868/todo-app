import { Component, inject, OnInit } from '@angular/core';
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

  todoService = inject(TodoService)

  editingTodoId: number | null = null;
  editedTitle = '';

  ngOnInit(): void {
    this.todoService.getTodos();
  }

  addTodo(title: string){
    const todo: Partial<TodoModel> = {
      title: title,
      completed: false
    };
    this.todoService.addTodo(todo).subscribe(() => {
      this.todoService.getTodos();
    });
  }

  deleteTodo(todo: TodoModel){
    this.todoService.deleteTodo(todo).subscribe(() => {
      this.todoService.getTodos();
    });
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
    this.todoService.updateTodo(updatedTodo).subscribe(() => {
      this.editingTodoId = null;
      this.editedTitle = '';
      this.todoService.getTodos();
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

    this.todoService.updateTodo(updatedTodo).subscribe(() => {
      this.todoService.getTodos();
    });
  }

}
