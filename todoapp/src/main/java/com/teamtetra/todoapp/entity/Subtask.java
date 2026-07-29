package com.teamtetra.todoapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
    name = "subtasks",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_subtasks_todo_title",
            columnNames = {"todo_id", "title"}
        )
    },
    indexes = {
        @Index(name = "idx_subtasks_todo_id", columnList = "todo_id")
    }
)
public class Subtask {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Database auto-generates the id
    private Long subtaskId; // Unique id for each subtask

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    @Column(nullable = false) // Column cannot be null in the database
    private String title; // The subtask name/title

    @Column(nullable = false) // Column cannot be null in the database
    private boolean completed;
 }

