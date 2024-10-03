package com.hackathon.saloocupacio.user_activity_manager.repository;

import com.hackathon.saloocupacio.user_activity_manager.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    boolean existsByName(String name);
}
