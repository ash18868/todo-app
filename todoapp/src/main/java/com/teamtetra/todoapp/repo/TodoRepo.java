package com.teamtetra.todoapp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamtetra.todoapp.entity.Todo;

@Repository
public interface TodoRepo extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUser_UserId(Long userId);
    Optional<Todo> findByTodoIdAndUser_UserId(Long todoId, Long userId);
    boolean existsByUser_UserIdAndTitle(Long userId, String title);
    boolean existsByUser_UserIdAndTitleAndTodoIdNot(Long userId, String title, Long excludedTodoId);
}
