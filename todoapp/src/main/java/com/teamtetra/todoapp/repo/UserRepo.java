package com.teamtetra.todoapp.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamtetra.todoapp.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String credential);
    Optional<User> findByUserId(Long credential);
}
