package com.teamtetra.todoapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.teamtetra.todoapp.dto.CreateTodoRequest;
import com.teamtetra.todoapp.dto.TodoResponse;
import com.teamtetra.todoapp.dto.UpdateTodoRequest;
import com.teamtetra.todoapp.entity.Todo;
import com.teamtetra.todoapp.entity.User;
import com.teamtetra.todoapp.exception.AddTodoFailure;
import com.teamtetra.todoapp.repo.SubtaskRepo;
import com.teamtetra.todoapp.repo.TodoRepo;
import com.teamtetra.todoapp.repo.UserRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepo todoRepo;
    private final UserRepo userRepo;
    private final SubtaskRepo subtaskRepo;

    private final AuthService authService;

    public TodoResponse addTodo(CreateTodoRequest requestBody, HttpServletRequest request){
        
        // Check for existing user
        Long userId = authService.extractUserId(request);
        User user = userRepo.findByUserId(userId).orElseThrow(() -> new AddTodoFailure("Could not find matching user id"));

        // Make sure todo doesn't already exist
        if(todoRepo.existsByUser_UserIdAndTitle(userId, requestBody.title())) {throw new AddTodoFailure("Todo already exists");}

        // Build the todo
        Todo todo = Todo.builder()
            .user(user)
            .title(requestBody.title())
            .build();
        
        // Add todo to DB
        Todo savedTodo = todoRepo.save(todo);
        return new TodoResponse(savedTodo.getTodoId(), savedTodo.getTitle(), savedTodo.isCompleted()); 
    }

    @Transactional // Makes the delete todo and subtasks one database operation
    public void deleteTodo(Long todoId, HttpServletRequest request){

        // Make sure Todo exists underneath it's user
        if (!todoRepo.findByTodoIdAndUser_UserId(todoId, authService.extractUserId(request)).isPresent()) {throw new AddTodoFailure("Could not find matching todo id");}
        
        // Delete children subtasks
        subtaskRepo.deleteAllByTodo_TodoId(todoId);
        // Delete todo
        todoRepo.deleteById(todoId);
    }

    public TodoResponse updateTodo(Long todoId, UpdateTodoRequest requestBody, HttpServletRequest request){

        // Make sure Todo exists underneath it's user
        Optional<Todo> optionalTodo = todoRepo.findByTodoIdAndUser_UserId(todoId, authService.extractUserId(request));
        if (optionalTodo.isEmpty()) {throw new AddTodoFailure("Could not find matching todo id");}

        // Make sure another Todo doesn't already share the same title
        Long userId = authService.extractUserId(request);
        if (todoRepo.existsByUser_UserIdAndTitleAndTodoIdNot(userId, requestBody.title(), todoId)) {throw new AddTodoFailure("Another todo already has this title");}

        Todo todo = optionalTodo.get(); // Convert to Todo
        
        // Update todo in DB
        todo.setTitle(requestBody.title());
        todo.setCompleted(requestBody.completed());
        Todo savedTodo = todoRepo.save(todo);
        return new TodoResponse(savedTodo.getTodoId(), savedTodo.getTitle(), savedTodo.isCompleted()); 
    }

    public List<TodoResponse> getTodos(HttpServletRequest request){

        Long userId = authService.extractUserId(request);

        // Check for existing user
        if (!userRepo.findByUserId(userId).isPresent()) {throw new AddTodoFailure("Could not find matching user id");}
        
        // Fetch todos
        List<Todo> todoList = todoRepo.findAllByUser_UserId(userId);

        // Stream todolist into a list of responses
        return todoList.stream()
            .map(todo -> new TodoResponse(
                todo.getTodoId(),
                todo.getTitle(),
                todo.isCompleted()
            ))
            .toList();
    }
}
