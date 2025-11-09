import { Component } from '@angular/core';
import { Todo, TodoService } from "./todo.service";
import { BehaviorSubject, Observable, combineLatest } from "rxjs";
import { FormControl } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { TodoDialogComponent } from './todo-dialog/todo-dialog.component';

@Component({
  selector: 'app-root',
  template: `
    <div class="page-container">
      <header class="header">
        <div class="header-content">
          <div class="header-text">
            <h1>Todo List</h1>
            <p class="subtitle">Here's a list of your tasks for this month</p>
          </div>
          <button class="user-avatar" mat-icon-button>
            <mat-icon>account_circle</mat-icon>
          </button>
        </div>
      </header>

      <div class="content">
        <div class="toolbar">
          <input 
            type="text" 
            class="filter-input" 
            placeholder="Filter tasks"
            [formControl]="searchControl">
          
          <div class="toolbar-actions">
            <button class="filter-btn" mat-button disabled>
              <mat-icon>add_circle_outline</mat-icon>
              Status
            </button>
            <button class="filter-btn" mat-button disabled>
              <mat-icon>add_circle_outline</mat-icon>
              Priority
            </button>
            <button class="view-btn" mat-button disabled>
              <mat-icon>view_list</mat-icon>
              View
            </button>
            <button class="add-task-btn" mat-raised-button (click)="openAddTodoDialog(); $event.stopPropagation()">
              Add Todo
            </button>
          </div>
        </div>

        <mat-progress-bar
          *ngIf="isLoading$ | async"
          mode="indeterminate"
          class="loading-bar">
        </mat-progress-bar>

        <div class="table-container">
          <table class="tasks-table">
            <thead>
              <tr>
                <th class="index-col">#</th>
                <th class="title-col">Title</th>
                <th class="priority-col">Priority</th>
                <th class="actions-col">Actions</th>
              </tr>
            </thead>
            <tbody>
              <app-todo-item *ngFor="let todo of filteredTodos$ | async; let i = index"
                             [item]="todo"
                             [index]="i + 1"
                             [isDeleting]="(deletingId$ | async) === todo.id"
                             [isDisabled]="(deletingId$ | async) !== null"
                             (delete)="deleteTodo(todo)">
              </app-todo-item>

              <tr *ngIf="(filteredTodos$ | async)?.length === 0 && !(isLoading$ | async)" class="no-results">
                <td colspan="4">
                  <div class="no-todos">
                    <mat-icon>search_off</mat-icon>
                    <p>No tasks found</p>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
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

  constructor(private todoService: TodoService, private dialog: MatDialog) {
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

  openAddTodoDialog(): void {
    const dialogRef = this.dialog.open(TodoDialogComponent, {
      width: '500px',
      height: 'fit-content',
      panelClass: 'center-modal'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Add the new todo to the list
        const currentTodos = this.todosSubject.value;
        this.todosSubject.next([...currentTodos, result]);
      }
    });
  }
}
