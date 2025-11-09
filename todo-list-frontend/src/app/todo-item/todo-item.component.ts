import {Component, Input, Output, EventEmitter} from '@angular/core';
import {Todo} from "../todo.service";

@Component({
  selector: 'app-todo-item',
  template: `
      <div class="todo-item-container" [class.deleting]="isDeleting" [class.disabled]="isDisabled" (click)="onClick()">
        <div class="task-indicator">
          {{ item.task }}
          <span *ngIf="isDeleting" class="deleting-indicator">×</span>
        </div>
        <div class="priority-indicator" [style.background-color]="color">
          {{ item.priority }}
        </div>
      </div>
  `,
  styleUrls: ['todo-item.component.scss']
})
export class TodoItemComponent {

  @Input() item!: Todo;
  @Input() isDeleting = false;
  @Input() isDisabled = false;

  @Output() delete = new EventEmitter<Todo>();

  get color() {
    switch (this.item.priority) {
      case 1:
        return 'green';
      case 2:
        return 'yellow';
      case 3:
        return 'red';
    }
  }

  onClick(): void {
    if (!this.isDisabled && !this.isDeleting) {
      this.delete.emit(this.item);
    }
  }
}
