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

import com.teamtetra.todoapp.dto.CreateSubtaskRequest;
import com.teamtetra.todoapp.dto.SubtaskResponse;
import com.teamtetra.todoapp.dto.UpdateSubtaskRequest;
import com.teamtetra.todoapp.exception.AddSubtaskFailure;
import com.teamtetra.todoapp.service.SubtaskService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SubtaskController {

    private final SubtaskService subtaskService;

    @PostMapping("/todo/{todoId}/subtask")
    public ResponseEntity<SubtaskResponse> addSubtask(@PathVariable Long todoId, @Valid @RequestBody CreateSubtaskRequest requestBody, HttpServletRequest request){
        SubtaskResponse response = subtaskService.addSubtask(todoId, requestBody, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/todo/{todoId}/subtask/{subtaskId}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long todoId, @PathVariable Long subtaskId, HttpServletRequest request){
        subtaskService.deleteSubtask(todoId, subtaskId, request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/todo/{todoId}/subtask/{subtaskId}")
    public ResponseEntity<SubtaskResponse> updateSubtask(@PathVariable Long todoId, @PathVariable Long subtaskId, @Valid @RequestBody UpdateSubtaskRequest requestBody, HttpServletRequest request){
        SubtaskResponse response = subtaskService.updateSubtask(todoId, subtaskId, requestBody, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/todo/{todoId}/subtask")
    public ResponseEntity<List<SubtaskResponse>> getSubtasks(@PathVariable Long todoId, HttpServletRequest request){
        List<SubtaskResponse> responses = subtaskService.getSubtasks(todoId, request);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @ExceptionHandler(AddSubtaskFailure.class)
    public ResponseEntity<String> handleAddSubtaskFailure(AddSubtaskFailure exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
