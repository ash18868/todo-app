import { Component, inject, Input, OnInit } from '@angular/core';
import { SubtaskService } from '../../services/subtask-service';
import { AsyncPipe } from '@angular/common';
import { SubtaskModel } from '../../models/subtask-model';
import { TodoModel } from '../../models/todo-model';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-subtask',
  imports: [AsyncPipe, FormsModule],
  templateUrl: './subtask.html',
  styleUrl: './subtask.css',
})
export class Subtask implements OnInit {

  @Input() todo!: TodoModel;

  subtaskService = inject(SubtaskService);
  subtasks$!: Observable<SubtaskModel[]>;

  editingSubtaskId: number | null = null;
  editedTitle = '';

  ngOnInit(): void {
    this.subtasks$ = this.subtaskService.getSubjectForTodo(this.todo.todoId);
    this.subtaskService.getSubtasks(this.todo);
  }

  addSubtask(title: string): void {
    const subtask: Partial<SubtaskModel> = {
      todoId: this.todo.todoId,
      title: title,
      completed: false
    };
    this.subtaskService.addSubtask(subtask).subscribe(() => {
      this.subtaskService.getSubtasks(this.todo);
    });
  }

  deleteSubtask(subtask: SubtaskModel): void {
    this.subtaskService.deleteSubtask(subtask).subscribe(() => {
      this.subtaskService.getSubtasks(this.todo);
    });
  }

  editSubtask(subtask: SubtaskModel): void {
    this.editingSubtaskId = subtask.subtaskId;
    this.editedTitle = subtask.title;
  }

  saveSubtask(subtask: SubtaskModel): void {
    const updatedSubtask: SubtaskModel = {
      ...subtask,
      title: this.editedTitle
    };
    this.subtaskService.updateSubtask(updatedSubtask).subscribe(() => {
      this.editingSubtaskId = null;
      this.editedTitle = '';
      this.subtaskService.getSubtasks(this.todo);
    });
  }

  cancelEdit(): void {
    this.editingSubtaskId = null;
    this.editedTitle = '';
  }

  toggleSubtaskCompleted(subtask: SubtaskModel, completed: boolean): void {
    const updatedSubtask: SubtaskModel = {
      ...subtask,
      completed: completed
    };
    this.subtaskService.updateSubtask(updatedSubtask).subscribe(() => {
      this.subtaskService.getSubtasks(this.todo);
    });
  }

}
