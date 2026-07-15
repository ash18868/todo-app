package com.teamtetra.todoapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamtetra.todoapp.entity.Todo;
import com.teamtetra.todoapp.entity.User;
import com.teamtetra.todoapp.exception.AddTodoFailure;
import com.teamtetra.todoapp.service.TodoService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TodoController{
    private final TodoService todoService;

    @PostMapping("/todo")
    public ResponseEntity<Void> addTodo(@RequestBody Todo todo, HttpServletRequest request){
        String userIdFromToken = (String) request.getAttribute("userId");
        todoService.addTodo(todo, Long.parseLong(userIdFromToken));
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @DeleteMapping("/todo")
    public ResponseEntity<Void> deleteTodo(@RequestBody Todo todo){
        todoService.deleteTodo(todo);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/todo")
    public ResponseEntity<Void> updateTodo(@RequestBody Todo todo){
        todoService.updateTodo(todo);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/todo")
    public ResponseEntity<List<Todo>> getTodos(HttpServletRequest request){
        String userIdFromToken = (String) request.getAttribute("userId");
        List<Todo> todoList = todoService.getTodos(Long.parseLong(userIdFromToken));
        return ResponseEntity.status(HttpStatus.OK).body(todoList);
    }

    @ExceptionHandler(AddTodoFailure.class)
    public ResponseEntity<String> handleAddTodoFailure(AddTodoFailure exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}