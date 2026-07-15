package com.teamtetra.todoapp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamtetra.todoapp.entity.Subtask;

@Repository
public interface SubtaskRepo extends JpaRepository<Subtask, Long> {
    List<Subtask> findByTodoId(Long todoId);
    //Can be changed to return Integer if that is more appropriate for todo_id
    Optional<Subtask> findByTitle(String credential);
    void deleteByTodoId(Long todoId);
}
