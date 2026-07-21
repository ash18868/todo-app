import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, of, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { SubtaskModel } from '../models/subtask-model';
import { TodoModel } from '../models/todo-model';

@Injectable({ providedIn: 'root' })
export class SubtaskService {

    private http = inject(HttpClient);
    private apiBaseUrl = environment.apiBaseUrl;

    // Map of todoId -> subtasks so each todo card has its own list
    private subtasksByTodo = new Map<number, BehaviorSubject<SubtaskModel[]>>();

    getSubjectForTodo(todoId: number): Observable<SubtaskModel[]> {
        if (!this.subtasksByTodo.has(todoId)) {
            this.subtasksByTodo.set(todoId, new BehaviorSubject<SubtaskModel[]>([]));
        }
        return this.subtasksByTodo.get(todoId)!.asObservable();
    }

    getSubtasks(todo: TodoModel): Observable<SubtaskModel[]> {
        return this.http
            .get<SubtaskModel[]>(`${this.apiBaseUrl}/subtask`, {
                params: { todoId: todo.todoId },
            })
            .pipe(
                tap(subtasks => {
                    this.getOrCreateSubject(todo.todoId).next(subtasks);
                }),
                catchError(error => {
                    console.error(
                    `Failed to load subtasks for todo ${todo.todoId}:`,
                    error,
                    );

                    this.getOrCreateSubject(todo.todoId).next([]);
                    return of([]);
                }),
            );
    }

    addSubtask(subtask: Partial<SubtaskModel>): Observable<SubtaskModel> {
        return this.http.post<SubtaskModel>(`${this.apiBaseUrl}/subtask`, subtask);
    }

    deleteSubtask(subtask: SubtaskModel): Observable<void> {
        return this.http.delete<void>(`${this.apiBaseUrl}/subtask`, { body: subtask });
    }

    updateSubtask(subtask: SubtaskModel): Observable<void> {
        return this.http.put<void>(`${this.apiBaseUrl}/subtask`, subtask);
    }

    clearSubtasksForTodo(todoId: number): void {
        const subject = this.subtasksByTodo.get(todoId);

        if (subject) {
            subject.complete();
            this.subtasksByTodo.delete(todoId);
        }
    }

    private getOrCreateSubject(todoId: number): BehaviorSubject<SubtaskModel[]> {
        if (!this.subtasksByTodo.has(todoId)) {
            this.subtasksByTodo.set(todoId, new BehaviorSubject<SubtaskModel[]>([]));
        }
        return this.subtasksByTodo.get(todoId)!;
    }
}
