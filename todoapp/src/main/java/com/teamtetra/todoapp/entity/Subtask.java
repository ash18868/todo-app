package com.teamtetra.todoapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "subtasks") 
@Data 
@NoArgsConstructor 
public class Subtask {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Database auto-generates the id
    private Long subtaskId; // Unique id for each subtask

    @JoinColumn(name = "todoId", nullable= false)
    private Long todoId; // foreign key

    @Column(nullable = false) // Column cannot be null in the database
    private String title; // The subtask name/title

    @Column(nullable = false) // Column cannot be null in the database
    private boolean completed = false; // Subtask starts as not completed


//     @ManyToOne(fetch = FetchType.LAZY, optional = false) // Many subtasks belong to one Todo
//     @JoinColumn(name = "todo_id", nullable = false) // Creates foreign key column todo_id
//     private Todo todo; // The Todo that this subtask belongs to
 }

