package com.todo.service;

import com.todo.entity.Todo;
import com.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTodos_shouldReturnAllTodos() {
        // Given
        Todo todo1 = new Todo("Task 1", 1);
        todo1.setId(1L);
        Todo todo2 = new Todo("Task 2", 2);
        todo2.setId(2L);
        List<Todo> expectedTodos = Arrays.asList(todo1, todo2);

        when(todoRepository.findAll()).thenReturn(expectedTodos);

        // When
        List<Todo> actualTodos = todoService.getAllTodos();

        // Then
        assertEquals(expectedTodos, actualTodos);
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void getTodoById_shouldReturnTodoWhenExists() {
        // Given
        Long todoId = 1L;
        Todo expectedTodo = new Todo("Test task", 1);
        expectedTodo.setId(todoId);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(expectedTodo));

        // When
        Optional<Todo> actualTodo = todoService.getTodoById(todoId);

        // Then
        assertTrue(actualTodo.isPresent());
        assertEquals(expectedTodo, actualTodo.get());
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    void getTodoById_shouldReturnEmptyOptionalWhenTodoDoesNotExist() {
        // Given
        Long todoId = 999L;
        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        // When
        Optional<Todo> actualTodo = todoService.getTodoById(todoId);

        // Then
        assertFalse(actualTodo.isPresent());
        verify(todoRepository, times(1)).findById(todoId);
    }

    @Test
    void createTodo_shouldSaveAndReturnTodo() {
        // Given
        Todo todoToCreate = new Todo("New task", 3);
        Todo savedTodo = new Todo("New task", 3);
        savedTodo.setId(1L);

        when(todoRepository.save(todoToCreate)).thenReturn(savedTodo);

        // When
        Todo actualTodo = todoService.createTodo(todoToCreate);

        // Then
        assertEquals(savedTodo, actualTodo);
        verify(todoRepository, times(1)).save(todoToCreate);
    }

    @Test
    void updateTodo_shouldUpdateAndReturnTodoWhenExists() {
        // Given
        Long todoId = 1L;
        Todo existingTodo = new Todo("Original task", 1);
        existingTodo.setId(todoId);
        Todo updateDetails = new Todo("Updated task", 2);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(existingTodo);

        // When
        Todo actualTodo = todoService.updateTodo(todoId, updateDetails);

        // Then
        assertNotNull(actualTodo);
        assertEquals(todoId, actualTodo.getId());
        assertEquals("Updated task", actualTodo.getTask());
        assertEquals(2, actualTodo.getPriority());
        verify(todoRepository, times(1)).findById(todoId);
        verify(todoRepository, times(1)).save(existingTodo);
    }

    @Test
    void updateTodo_shouldReturnNullWhenTodoDoesNotExist() {
        // Given
        Long todoId = 999L;
        Todo updateDetails = new Todo("Updated task", 2);

        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        // When
        Todo actualTodo = todoService.updateTodo(todoId, updateDetails);

        // Then
        assertNull(actualTodo);
        verify(todoRepository, times(1)).findById(todoId);
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void deleteTodo_shouldReturnTrueWhenTodoExists() {
        // Given
        Long todoId = 1L;
        when(todoRepository.existsById(todoId)).thenReturn(true);

        // When
        boolean result = todoService.deleteTodo(todoId);

        // Then
        assertTrue(result);
        verify(todoRepository, times(1)).existsById(todoId);
        verify(todoRepository, times(1)).deleteById(todoId);
    }

    @Test
    void deleteTodo_shouldReturnFalseWhenTodoDoesNotExist() {
        // Given
        Long todoId = 999L;
        when(todoRepository.existsById(todoId)).thenReturn(false);

        // When
        boolean result = todoService.deleteTodo(todoId);

        // Then
        assertFalse(result);
        verify(todoRepository, times(1)).existsById(todoId);
        verify(todoRepository, never()).deleteById(todoId);
    }
}
