package com.teamtetra.todoapp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamtetra.todoapp.entity.Subtask;
import com.teamtetra.todoapp.entity.Todo;

@Repository
public interface SubtaskRepo extends JpaRepository<Subtask, Long> {
    List<Subtask> findAllByTodo_TodoId(Long todoId);
    void deleteAllByTodo_TodoId(Long todoId);
    Optional<Subtask> findBySubtaskIdAndTodo_TodoId(Long subtaskId, Long todoId);
    boolean existsByTodo_TodoIdAndTitle(Long todoId, String title);
    boolean existsByTodo_TodoIdAndTitleAndSubtaskIdNot(Long todoId, String title, Long excludedSubtaskId);
    boolean existsByTodo_TodoIdAndSubtaskId(Long todoId, Long subtaskId);
}
