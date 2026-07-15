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

import com.teamtetra.todoapp.entity.Subtask;
import com.teamtetra.todoapp.exception.AddSubtaskFailure;
import com.teamtetra.todoapp.service.SubtaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SubtaskController {

    private final SubtaskService subtaskService;

    @PostMapping("/subtask")
    public ResponseEntity<Void> addSubtask(@RequestBody Subtask subtask){
        subtaskService.addSubtask(subtask);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @DeleteMapping("/subtask")
    public ResponseEntity<Void> deleteSubtask(@RequestBody Subtask subtask){
        subtaskService.deleteSubtask(subtask);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/subtask")
    public ResponseEntity<Void> updateSubtask(@RequestBody Subtask subtask){
        subtaskService.updateSubtask(subtask);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/subtask")
    public ResponseEntity<List<Subtask>> getSubtasks(@RequestParam Long todoId){
        List<Subtask> subtaskList = subtaskService.getSubtasks(todoId);
        return ResponseEntity.status(HttpStatus.OK).body(subtaskList);
    }

    @ExceptionHandler(AddSubtaskFailure.class)
    public ResponseEntity<String> handleAddSubtaskFailure(AddSubtaskFailure exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
