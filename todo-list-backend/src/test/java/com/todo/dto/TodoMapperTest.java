package com.todo.dto;

import com.todo.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoMapperTest {

    @Autowired
    private TodoMapper todoMapper;

    @Test
    void toResponseDTO_shouldMapTodoToTodoResponseDTO() {
        // Given
        Todo todo = new Todo("Test task", 1);
        todo.setId(1L);

        // When
        TodoResponseDTO responseDTO = todoMapper.toResponseDTO(todo);

        // Then
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("Test task", responseDTO.getTask());
        assertEquals(1, responseDTO.getPriority());
    }

    @Test
    void toResponseDTO_shouldReturnNullWhenTodoIsNull() {
        // When
        TodoResponseDTO responseDTO = todoMapper.toResponseDTO(null);

        // Then
        assertNull(responseDTO);
    }

    @Test
    void toResponseDTOList_shouldMapListOfTodosToListOfTodoResponseDTOs() {
        // Given
        Todo todo1 = new Todo("Task 1", 1);
        todo1.setId(1L);
        Todo todo2 = new Todo("Task 2", 2);
        todo2.setId(2L);
        List<Todo> todos = Arrays.asList(todo1, todo2);

        // When
        List<TodoResponseDTO> responseDTOs = todoMapper.toResponseDTOList(todos);

        // Then
        assertNotNull(responseDTOs);
        assertEquals(2, responseDTOs.size());
        assertEquals("Task 1", responseDTOs.get(0).getTask());
        assertEquals("Task 2", responseDTOs.get(1).getTask());
    }

    @Test
    void toResponseDTOList_shouldReturnNullWhenTodosListIsNull() {
        // When
        List<TodoResponseDTO> responseDTOs = todoMapper.toResponseDTOList(null);

        // Then
        assertNull(responseDTOs);
    }

    @Test
    void toEntity_shouldMapTodoRequestDTOToTodo() {
        // Given
        TodoRequestDTO requestDTO = new TodoRequestDTO("New task", 3);

        // When
        Todo todo = todoMapper.toEntity(requestDTO);

        // Then
        assertNotNull(todo);
        assertNull(todo.getId()); // ID should not be set from request DTO
        assertEquals("New task", todo.getTask());
        assertEquals(3, todo.getPriority());
    }

    @Test
    void toEntity_shouldReturnNullWhenTodoRequestDTOIsNull() {
        // When
        Todo todo = todoMapper.toEntity(null);

        // Then
        assertNull(todo);
    }

    @Test
    void updateEntityFromDTO_shouldUpdateTodoFields() {
        // Given
        Todo todo = new Todo("Original task", 1);
        todo.setId(1L);
        TodoRequestDTO requestDTO = new TodoRequestDTO("Updated task", 2);

        // When
        todoMapper.updateEntityFromDTO(todo, requestDTO);

        // Then
        assertEquals(1L, todo.getId()); // ID should remain unchanged
        assertEquals("Updated task", todo.getTask());
        assertEquals(2, todo.getPriority());
    }

    @Test
    void updateEntityFromDTO_shouldHandleNullParameters() {
        // Given
        Todo todo = new Todo("Original task", 1);

        // When - should not throw exception
        todoMapper.updateEntityFromDTO(todo, null);
        todoMapper.updateEntityFromDTO(null, new TodoRequestDTO("test", 1));

        // Then - no assertions needed, just verifying no exceptions
    }
}
