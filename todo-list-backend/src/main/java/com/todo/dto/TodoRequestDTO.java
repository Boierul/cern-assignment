package com.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TodoRequestDTO {

    @NotBlank(message = "Task description is required")
    private String task;

    @NotNull(message = "Priority is required")
    private Integer priority;

    public TodoRequestDTO() {}

    public TodoRequestDTO(String task, Integer priority) {
        this.task = task;
        this.priority = priority;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
