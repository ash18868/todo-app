package com.teamtetra.todoapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@Table(name = "Todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId; //! Primary Key

    //@ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    private boolean completed;
    // @ManyToOne
    // @JoinColumn(name = "id", nullable = false)
    // private User user;
}
