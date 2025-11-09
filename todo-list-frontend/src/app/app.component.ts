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
      <app-todo-item *ngFor="let todo of filteredTodos$ | async"
                     [item]="todo"
                     [isDeleting]="(deletingId$ | async) === todo.id"
                     [isDisabled]="(deletingId$ | async) !== null"
                     (delete)="deleteTodo(todo)"></app-todo-item>
    </div>
  `,
  styleUrls: ['app.component.scss']
})
export class AppComponent {

  private todosSubject = new BehaviorSubject<Todo[]>([]);
  readonly todos$ = this.todosSubject.asObservable();
  readonly isLoading$ = new BehaviorSubject<boolean>(true);
  readonly deletingId$ = new BehaviorSubject<number | null>(null);

  readonly searchControl = new FormControl('');
  readonly filteredTodos$: Observable<Todo[]>;

  constructor(private todoService: TodoService) {
    this.loadTodos();

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

  private loadTodos(): void {
    this.todoService.getAll().subscribe(todos => {
      this.todosSubject.next(todos);
      this.isLoading$.next(false);
    });
  }

  deleteTodo(todo: Todo): void {
    this.deletingId$.next(todo.id);
    this.todoService.remove(todo.id).subscribe({
      next: () => {
        // Refresh the todos list after successful deletion
        this.loadTodos();
        this.deletingId$.next(null);
      },
      error: (error) => {
        console.error('Failed to delete todo:', error);
        alert('Failed to delete todo. Please try again.');
        this.deletingId$.next(null);
      }
    });
  }
}
