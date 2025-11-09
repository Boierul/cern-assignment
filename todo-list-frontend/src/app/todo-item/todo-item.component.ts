import {Component, Input, Output, EventEmitter} from '@angular/core';
import {Todo} from "../todo.service";

@Component({
  selector: 'app-todo-item',
  template: `
    <tr [class.deleting]="isDeleting" [class.disabled]="isDisabled">
      <td class="index-col">{{ index }}</td>
      <td class="title-col">{{ item.task }}</td>
      <td class="priority-col">
        <div class="priority-badge" [class]="getPriorityClass()">
          <mat-icon class="priority-icon" *ngIf="getPriorityIcon()">{{ getPriorityIcon() }}</mat-icon>
          <span>{{ getPriorityText() }}</span>
        </div>
      </td>
      <td class="actions-col">
        <button class="delete-btn"
                mat-raised-button
                color="warn"
                (click)="onClick()"
                [disabled]="isDisabled || isDeleting">
          <mat-icon>{{ isDeleting ? 'hourglass_empty' : 'delete' }}</mat-icon>
          {{ isDeleting ? 'Deleting...' : 'Delete' }}
        </button>
      </td>
    </tr>
  `,
  styleUrls: ['todo-item.component.scss']
})
export class TodoItemComponent {

  @Input() item!: Todo;
  @Input() index!: number;
  @Input() isDeleting = false;
  @Input() isDisabled = false;

  @Output() delete = new EventEmitter<Todo>();

  getPriorityClass(): string {
    switch (this.item.priority) {
      case 1:
        return 'priority-low';
      case 2:
        return 'priority-medium';
      case 3:
        return 'priority-high';
      default:
        return 'priority-low';
    }
  }

  getPriorityIcon(): string {
    // Return empty string to remove arrows
    return '';
  }

  getPriorityText(): string {
    switch (this.item.priority) {
      case 1:
        return 'Low';
      case 2:
        return 'Medium';
      case 3:
        return 'High';
      default:
        return 'Low';
    }
  }

  onClick(): void {
    if (!this.isDisabled && !this.isDeleting) {
      this.delete.emit(this.item);
    }
  }
}
