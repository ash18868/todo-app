import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { TodoModel } from '../models/todo-model';
import { BehaviorSubject, catchError, Observable, of, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TodoService {

    private http = inject(HttpClient);
    private apiBaseUrl = environment.apiBaseUrl;

    private todoSubject: BehaviorSubject<TodoModel[]>;

    todo$: Observable<TodoModel[]>;

    private emptyTodos: TodoModel[] = [];
    
    constructor(){
        this.todoSubject = new BehaviorSubject<TodoModel[]>([]);
        this.todo$ = this.todoSubject.asObservable();
    }


    getTodos() {
        this.http.get<TodoModel[]>(`${this.apiBaseUrl}/todo`,)
        .pipe(
            tap(todoData => this.todoSubject.next(todoData)),
            catchError(error => {
                console.log(`Something went wrong: ${error}`)
                this.todoSubject.next(this.emptyTodos)
                return of(null);
            })
        ).subscribe()
    }

    addTodo(todo: Partial<TodoModel>) {
        return this.http.post<TodoModel>(`${this.apiBaseUrl}/todo`, todo);
    }

    deleteTodo(todo: TodoModel){
        return this.http.delete<void>(`${this.apiBaseUrl}/todo`, {
            body: todo
        });
    }
    updateTodo(todo: TodoModel){
        return this.http.put<void>(`${this.apiBaseUrl}/todo`, todo);
    }
}
