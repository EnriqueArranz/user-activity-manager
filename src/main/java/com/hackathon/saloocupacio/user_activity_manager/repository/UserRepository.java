package com.hackathon.saloocupacio.user_activity_manager.repository;

import com.hackathon.saloocupacio.user_activity_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
