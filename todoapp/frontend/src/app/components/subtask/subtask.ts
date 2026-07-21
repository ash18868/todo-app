import { Component, DestroyRef, inject, Input, OnInit, OnDestroy } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
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
export class Subtask implements OnInit, OnDestroy {

  private readonly destroyRef = inject(DestroyRef);

  @Input() todo!: TodoModel; //Comes in from the parent todo in <app-subtask>

  subtaskService = inject(SubtaskService);
  subtasks$!: Observable<SubtaskModel[]>;

  editingSubtaskId: number | null = null;
  editedTitle = '';

  ngOnInit(): void {
    this.subtasks$ =
      this.subtaskService.getSubjectForTodo(this.todo.todoId);

    this.refreshSubtasks();
  }

  private refreshSubtasks(): void {
    this.subtaskService
      .getSubtasks(this.todo)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
  }

  ngOnDestroy(): void {
    this.subtaskService.clearSubtasksForTodo(this.todo.todoId);
  }

  addSubtask(title: string): void {
    const trimmedTitle = title.trim();
    if (!trimmedTitle) { // Catches empty todo
      return;
    }

    const subtask: Partial<SubtaskModel> = {
      todoId: this.todo.todoId,
      title: trimmedTitle,
      completed: false
    };
    this.subtaskService.addSubtask(subtask)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshSubtasks());
  }

  deleteSubtask(subtask: SubtaskModel): void {
    this.subtaskService.deleteSubtask(subtask)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshSubtasks());
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
    this.subtaskService.updateSubtask(updatedSubtask)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.editingSubtaskId = null;
        this.editedTitle = '';
        this.refreshSubtasks();
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
    this.subtaskService.updateSubtask(updatedSubtask)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshSubtasks());
  }
}
