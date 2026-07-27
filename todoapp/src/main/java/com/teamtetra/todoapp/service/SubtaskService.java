package com.teamtetra.todoapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.teamtetra.todoapp.dto.CreateSubtaskRequest;
import com.teamtetra.todoapp.dto.SubtaskResponse;
import com.teamtetra.todoapp.dto.UpdateSubtaskRequest;
import com.teamtetra.todoapp.dto.UpdateTodoRequest;
import com.teamtetra.todoapp.entity.Subtask;
import com.teamtetra.todoapp.entity.Todo;
import com.teamtetra.todoapp.exception.AddSubtaskFailure;
import com.teamtetra.todoapp.exception.AddTodoFailure;
import com.teamtetra.todoapp.repo.SubtaskRepo;
import com.teamtetra.todoapp.repo.TodoRepo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubtaskService {
    private final SubtaskRepo subtaskRepo;
    private final TodoRepo todoRepo;

    private final AuthService authService;

    public SubtaskResponse addSubtask(Long todoId, CreateSubtaskRequest requestBody, HttpServletRequest request){

        // Make sure request has authorization
        Long userId = authService.extractUserId(request);
        Todo todo = getAuthorizedTodo(todoId, userId);

        // Build the subtask
        Subtask subtask = Subtask.builder()
                    .todo(todo)
                    .title(requestBody.title())
                    .build();

        // Make sure subtask doesn't already exist
        if(subtaskRepo.existsByTodo_TodoIdAndTitle(todoId, requestBody.title())) {throw new AddSubtaskFailure("Subtask already exists");}

        // Add Subtask to DB
        Subtask savedSubtask = subtaskRepo.save(subtask);
        return new SubtaskResponse(savedSubtask.getSubtaskId(), savedSubtask.getTodo().getTodoId(), savedSubtask.getTitle(), savedSubtask.isCompleted()); 

    }

    public void deleteSubtask(Long todoId, Long subtaskId, HttpServletRequest request){

        // Make sure request has authorization
        Long userId = authService.extractUserId(request);
        getAuthorizedTodo(todoId, userId);

        // Make sure subtask exists underneath its Todo
        if (!subtaskRepo.existsByTodo_TodoIdAndSubtaskId(todoId, subtaskId)) {throw new AddSubtaskFailure("Subtask does not exist");}    

        // Delete subtask
        subtaskRepo.deleteById(subtaskId);
    }

    public SubtaskResponse updateSubtask(Long todoId, Long subtaskId,  UpdateSubtaskRequest requestBody, HttpServletRequest request){

        // Make sure request has authorization
        Long userId = authService.extractUserId(request);
        Todo todo = getAuthorizedTodo(todoId, userId); 

        // Make sure subtask exists
        Optional<Subtask> optionalSubtask = subtaskRepo.findBySubtaskIdAndTodo_TodoId(subtaskId, todoId);
        if(optionalSubtask.isEmpty()){throw new AddSubtaskFailure("Could not find matching subtask id");}

        // Make sure another Subtask under that todo doesn't already share the same title
        if(subtaskRepo.existsByTodo_TodoIdAndTitleAndSubtaskIdNot(todoId, requestBody.title(), subtaskId)) {throw new AddSubtaskFailure("Another subtask already has this title");}
        
        Subtask subtask = optionalSubtask.get(); // Convert to subtask

        // Update subtask in DB
        subtask.setTitle(requestBody.title());
        subtask.setCompleted(requestBody.completed());
        Subtask savedSubtask = subtaskRepo.save(subtask);
        return new SubtaskResponse(savedSubtask.getSubtaskId(), savedSubtask.getTodo().getTodoId(), savedSubtask.getTitle(), savedSubtask.isCompleted());
    }

    public List<SubtaskResponse> getSubtasks(Long todoId, HttpServletRequest request){

        // Make sure request has authorization
        Long userId = authService.extractUserId(request);
        Todo todo = getAuthorizedTodo(todoId, userId);
        
        // Fetch todos
        List<Subtask> subtaskList = subtaskRepo.findAllByTodo_TodoId(todoId);

        // Stream subtaskList into a list of responses
        return subtaskList.stream()
            .map(subtask -> new SubtaskResponse(
                subtask.getSubtaskId(),
                todo.getTodoId(),
                subtask.getTitle(),
                subtask.isCompleted()
            ))
            .toList();
    }

    // Helper Method: make sure parent todo exists and verify user is authorized
    private Todo getAuthorizedTodo(Long todoId, Long userId) {
    return todoRepo.findByTodoIdAndUser_UserId(todoId, userId)
        .orElseThrow(() ->
            new AddSubtaskFailure("User is not authorized or parent todo does not exist"));
    }

}
