package com.todo.dto;

public class TodoResponseDTO {

    private Long id;
    private String task;
    private Integer priority;

    public TodoResponseDTO() {}

    public TodoResponseDTO(Long id, String task, Integer priority) {
        this.id = id;
        this.task = task;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
