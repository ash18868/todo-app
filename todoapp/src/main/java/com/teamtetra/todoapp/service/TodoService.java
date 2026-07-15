package com.teamtetra.todoapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.teamtetra.todoapp.entity.Todo;
import com.teamtetra.todoapp.exception.AddTodoFailure;
import com.teamtetra.todoapp.repo.SubtaskRepo;
import com.teamtetra.todoapp.repo.TodoRepo; //Switch later
import com.teamtetra.todoapp.repo.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepo todoRepo;
    private final UserRepo userRepo;
    private final SubtaskRepo subtaskRepo;

    public void addTodo(Todo todo, Long userId){
        //check for existing user
        if (userRepo.findByUserId(userId).isPresent())
        {
            todo.setUserId(userId);
            todoRepo.save(todo);
        }
        else{
            throw new AddTodoFailure("Could not find matching user id");
        }
    }

    @Transactional // Makes the delete todo and subtasks one database operation
    public void deleteTodo(Todo todo){

        if (todoRepo.findById(todo.getTodoId()).isPresent())
        {
            subtaskRepo.deleteByTodoId(todo.getTodoId());
            todoRepo.delete(todo);
        }
        else{
            throw new AddTodoFailure("Could not find matching todo id");
        }
    }

    public void updateTodo(Todo todo){

        //Optional<Todo>

        if (todoRepo.findById(todo.getTodoId()).isPresent())
        {
            todoRepo.save(todo);
        }
        else{
            throw new AddTodoFailure("Could not find matching todo id");
        }

    }

    public List<Todo> getTodos(Long userId){

        //check for existing user
        if (userRepo.findByUserId(userId).isPresent())
        {
            List<Todo> todoList = todoRepo.findByUserId(userId);

            if (todoList.isEmpty())
            {
                throw new AddTodoFailure("Could not find any todos for matching user id" + userId);
            }
            else{
                return todoList;
            }
        }
        else{
            throw new AddTodoFailure("Could not find matching user id");
        }

    }
}
