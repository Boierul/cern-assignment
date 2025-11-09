import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TodoService } from '../todo.service';

@Component({
  selector: 'app-todo-dialog',
  templateUrl: './todo-dialog.component.html',
  styleUrls: ['./todo-dialog.component.scss']
})
export class TodoDialogComponent {
  todoForm: FormGroup;
  isSubmitting = false;

  priorities = [
    { value: 1, label: 'Low', icon: 'south' },
    { value: 2, label: 'Medium', icon: 'east' },
    { value: 3, label: 'High', icon: 'north' }
  ];

  constructor(
    private dialogRef: MatDialogRef<TodoDialogComponent>,
    private fb: FormBuilder,
    private todoService: TodoService
  ) {
    this.todoForm = this.fb.group({
      task: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(255)]],
      priority: [1, Validators.required]
    });
  }

  onSubmit(): void {
    if (this.todoForm.valid) {
      this.isSubmitting = true;
      const { task, priority } = this.todoForm.value;

      this.todoService.create(task, priority).subscribe({
        next: (newTodo) => {
          this.dialogRef.close(newTodo);
        },
        error: (error) => {
          console.error('Failed to create todo:', error);
          alert('Failed to create todo. Please try again.');
          this.isSubmitting = false;
        }
      });
    } else {
      this.todoForm.markAllAsTouched();
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
