package com.todo.controller;

import com.todo.dto.TodoMapper;
import com.todo.dto.TodoRequestDTO;
import com.todo.dto.TodoResponseDTO;
import com.todo.entity.Todo;
import com.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/todos")
public class TodoController {

    private final TodoService todoService;
    private final TodoMapper todoMapper;

    @Autowired
    public TodoController(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    @GetMapping
    public ResponseEntity<?> getTodos(@RequestParam(required = false) Long id) {
        if (id != null) {
            return todoService.getTodoById(id)
                    .map(todo -> ResponseEntity.ok(todoMapper.toResponseDTO(todo)))
                    .orElse(ResponseEntity.notFound().build());
        } else {
            List<Todo> todos = todoService.getAllTodos();
            return ResponseEntity.ok(todoMapper.toResponseDTOList(todos));
        }
    }

    @PostMapping
    public ResponseEntity<TodoResponseDTO> createTodo(@Valid @RequestBody TodoRequestDTO todoRequestDTO) {
        Todo todo = todoMapper.toEntity(todoRequestDTO);
        Todo createdTodo = todoService.createTodo(todo);
        TodoResponseDTO responseDTO = todoMapper.toResponseDTO(createdTodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping
    public ResponseEntity<TodoResponseDTO> updateTodo(@RequestParam Long id, @Valid @RequestBody TodoRequestDTO todoRequestDTO) {
        Todo todoDetails = todoMapper.toEntity(todoRequestDTO);
        Todo updatedTodo = todoService.updateTodo(id, todoDetails);
        if (updatedTodo != null) {
            TodoResponseDTO responseDTO = todoMapper.toResponseDTO(updatedTodo);
            return ResponseEntity.ok(responseDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTodo(@RequestParam Long id) {
        boolean deleted = todoService.deleteTodo(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
