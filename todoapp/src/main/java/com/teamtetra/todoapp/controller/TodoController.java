package com.teamtetra.todoapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamtetra.todoapp.dto.CreateTodoRequest;
import com.teamtetra.todoapp.dto.TodoResponse;
import com.teamtetra.todoapp.dto.UpdateTodoRequest;
import com.teamtetra.todoapp.exception.AddTodoFailure;
import com.teamtetra.todoapp.service.TodoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TodoController{
    /* 
        HttpServletRequest is being used to grab the Authorization header
        RequestBody is being used to grab the body
        PathVariable is being used to grab the todoId from the url path
    */
    private final TodoService todoService;

    @PostMapping("/todo")
    public ResponseEntity<TodoResponse> addTodo(@Valid @RequestBody CreateTodoRequest requestBody, HttpServletRequest request){
        TodoResponse response = todoService.addTodo(requestBody, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId, HttpServletRequest request){
        todoService.deleteTodo(todoId, request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/todo/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long todoId, @Valid @RequestBody UpdateTodoRequest requestBody, HttpServletRequest request){
        TodoResponse response = todoService.updateTodo(todoId, requestBody, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/todo")
    public ResponseEntity<List<TodoResponse>> getTodos(HttpServletRequest request){
        List<TodoResponse> responses = todoService.getTodos(request);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @ExceptionHandler(AddTodoFailure.class)
    public ResponseEntity<String> handleAddTodoFailure(AddTodoFailure exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    
}