package com.todo.dto;

import com.todo.entity.Todo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodoMapper {

    public TodoResponseDTO toResponseDTO(Todo todo) {
        if (todo == null) {
            return null;
        }
        return new TodoResponseDTO(todo.getId(), todo.getTask(), todo.getPriority());
    }

    public List<TodoResponseDTO> toResponseDTOList(List<Todo> todos) {
        if (todos == null) {
            return null;
        }
        return todos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Todo toEntity(TodoRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        Todo todo = new Todo();
        todo.setTask(requestDTO.getTask());
        todo.setPriority(requestDTO.getPriority());
        return todo;
    }

    public void updateEntityFromDTO(Todo todo, TodoRequestDTO requestDTO) {
        if (todo != null && requestDTO != null) {
            todo.setTask(requestDTO.getTask());
            todo.setPriority(requestDTO.getPriority());
        }
    }
}
