import { Component } from '@angular/core';
import { Todo, TodoService } from "./todo.service";
import { BehaviorSubject, Observable, combineLatest } from "rxjs";
import { FormControl } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  template: `
    <div class="title">
      <h1>
        A list of TODOs
      </h1>
    </div>
    <div class="list">
      <label for="search">Search...</label>
      <input id="search" type="text" [formControl]="searchControl">
      <app-progress-bar *ngIf="isLoading$ | async"></app-progress-bar>
      <app-todo-item *ngFor="let todo of filteredTodos$ | async" [item]="todo"></app-todo-item>
    </div>
  `,
  styleUrls: ['app.component.scss']
})
export class AppComponent {

  readonly todos$: Observable<Todo[]>;
  readonly isLoading$ = new BehaviorSubject<boolean>(true);
  
  readonly searchControl = new FormControl('');
  readonly filteredTodos$: Observable<Todo[]>;

  constructor(todoService: TodoService) {
    this.todos$ = todoService.getAll();
    this.todos$.subscribe(() => this.isLoading$.next(false));

    this.filteredTodos$ = combineLatest([
      this.todos$,
      this.searchControl.valueChanges.pipe(startWith(''))
    ]).pipe(
      map(([todos, searchTerm]) =>
        todos.filter(todo =>
          todo.task.toLowerCase().includes((searchTerm || '').toLowerCase())
        )
      )
    );
  }
}
