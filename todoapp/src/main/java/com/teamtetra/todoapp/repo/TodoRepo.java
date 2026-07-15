package com.teamtetra.todoapp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamtetra.todoapp.entity.Todo;

@Repository
public interface TodoRepo extends JpaRepository<Todo, Long> {
    List<Todo> findByUserId(Long userId);
    //Can be changed to return Integer if that is more appropriate for todo_id
    Optional<Todo> findByTitle(String credential);
    Optional<Todo> findByTodoId(Long todoId);
}
