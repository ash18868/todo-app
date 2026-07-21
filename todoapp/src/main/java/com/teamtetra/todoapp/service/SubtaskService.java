package com.teamtetra.todoapp.service;



import java.util.List;

import org.springframework.stereotype.Service;

import com.teamtetra.todoapp.entity.Subtask;
import com.teamtetra.todoapp.exception.AddSubtaskFailure;
import com.teamtetra.todoapp.exception.AddTodoFailure;
import com.teamtetra.todoapp.repo.SubtaskRepo;
import com.teamtetra.todoapp.repo.TodoRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubtaskService {
    private final SubtaskRepo subtaskRepo;
    private final TodoRepo todoRepo;

        public void addSubtask(Subtask subtask){
        //check for existing user
        if (todoRepo.findByTodoId(subtask.getTodoId()).isPresent())
        {
            if(subtaskRepo.findByTitle(subtask.getTitle()).isPresent()){
                throw new AddTodoFailure("Subtask already exists");
            }
            subtaskRepo.save(subtask);
        }
        else{
            throw new AddSubtaskFailure("Could not find matching todo id");
        }
    }

    public void deleteSubtask(Subtask subtask){

        if (subtaskRepo.findById(subtask.getSubtaskId()).isPresent())
        {
            subtaskRepo.delete(subtask);
        }
        else{
            throw new AddSubtaskFailure("Could not find matching subtask id");
        }
    }

    public void updateSubtask(Subtask subtask){

        //Optional<Subtask>

        if (subtaskRepo.findById(subtask.getSubtaskId()).isPresent())
        {
            subtaskRepo.save(subtask);
        }
        else{
            throw new AddSubtaskFailure("Could not find matching subtask id");
        }

    }

    public List<Subtask> getSubtasks(Long todoId){

        if (todoRepo.findByTodoId(todoId).isEmpty()) {
            throw new AddSubtaskFailure("Could not find matching todo id");
        }

        return subtaskRepo.findByTodoId(todoId);

    }

}
