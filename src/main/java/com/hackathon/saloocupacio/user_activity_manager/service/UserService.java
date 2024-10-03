package com.hackathon.saloocupacio.user_activity_manager.service;

import com.hackathon.saloocupacio.user_activity_manager.exception.ResourceNotFoundException;
import com.hackathon.saloocupacio.user_activity_manager.model.Activity;
import com.hackathon.saloocupacio.user_activity_manager.model.User;
import com.hackathon.saloocupacio.user_activity_manager.repository.ActivityRepository;
import com.hackathon.saloocupacio.user_activity_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private ActivityService activityService;

    public User createUser(User user) {
        logger.info("Creating new user: {}", user.getName());
        return userRepository.save(user);
    }

    public void updateUser(Long id, User userDetails) {
        if (id == null || userDetails == null) {
            throw new RuntimeException("Invalid input");
        }
        User user = userRepository.findById(id).orElseThrow();
        user.setName(userDetails.getName());
        user.setSurname(userDetails.getSurname());
        user.setAge(userDetails.getAge());
        user.setEmail(userDetails.getEmail());
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        // Remove the user from all associated activities
        for (Activity activity : user.getActivities()) {
            activity.getUsers().remove(user);
            activityRepository.save(activity);
        }
        logger.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    public void signUpToActivity(String email, Long activityId) {
        // Buscar usuario por email
        Optional<User> selectedUser = null;
        selectedUser = userRepository.findByEmail(email);
        Optional<Activity> selectedActivity = null;
        selectedActivity = activityRepository.findById(activityId);
        // Añadir actividad al usuario
        selectedUser.get().getActivities().add(selectedActivity.get());
        // Guardar usuario con la actividad asociada
        userRepository.save(selectedUser.get());
    }
    public void signUpUserToActivity(String email, Long activityId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Activity activity = activityService.getActivityById(activityId);
        user.getActivities().add(activity);
        userRepository.save(user);
    }

}