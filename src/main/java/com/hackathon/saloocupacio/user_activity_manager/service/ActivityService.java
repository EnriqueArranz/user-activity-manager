package com.hackathon.saloocupacio.user_activity_manager.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.saloocupacio.user_activity_manager.model.Activity;
import com.hackathon.saloocupacio.user_activity_manager.model.SignUpResponse;
import com.hackathon.saloocupacio.user_activity_manager.model.User;
import com.hackathon.saloocupacio.user_activity_manager.repository.ActivityRepository;
import com.hackathon.saloocupacio.user_activity_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public void deleteActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        for (User user : activity.getUsers()) {
            user.getActivities().remove(activity);
            userRepository.save(user);
        }
        activityRepository.delete(activity);
    }

    public List<Activity> importActivities(File jsonFile) throws IOException {
        try {
            List<Activity> activities = objectMapper.readValue(jsonFile,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, Activity.class));
            return activityRepository.saveAll(activities);
        } catch (IOException e) {
            Activity activity = objectMapper.readValue(jsonFile, Activity.class);
            List<Activity> result = new ArrayList<>();
            result.add(activityRepository.save(activity));
            return result;
        }
    }

    public void exportActivities(File jsonFile) throws IOException {
        List<Activity> activities = activityRepository.findAll();
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        writer.writeValue(jsonFile, activities);
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id).orElseThrow();
    }

    public void updateActivity(Long id, Activity activity) {
        Activity existingActivity = activityRepository.findById(id).orElseThrow();
        existingActivity.setName(activity.getName());
        existingActivity.setDescription(activity.getDescription());
        existingActivity.setMaxCapacity(activity.getMaxCapacity());
        activityRepository.save(existingActivity);
    }

    public Activity findById(Long id) {
        return activityRepository.findById(id).orElseThrow();
    }

    public SignUpResponse signUpUserInActivity(Long activityId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Activity activity = activityRepository.findById(activityId).orElseThrow();
        if (user.getActivities().contains(activity)) {
            return new SignUpResponse(false, user.getName()+" already signed up for  " + activity.getName());
        }
        user.getActivities().add(activity);
        userRepository.save(user);
        return new SignUpResponse(true, "User signed up for  " + activity.getName());
    }


    public Optional<List<User>> getUsersInActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow();
        return Optional.ofNullable(activity.getUsers() != null ? new ArrayList<>(activity.getUsers()) : null);
    }
    public boolean existsActivityWithName(String name) {
        return activityRepository.existsByName(name);
    }

    public void removeUserFromActivity(Long activityId, Long userId) {
        // Assuming you have a repository for activities and users
        Activity activity = activityRepository.findById(activityId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        activity.getUsers().remove(user);
        user.getActivities().remove(activity);
        activityRepository.save(activity);
    }
}