package com.hackathon.saloocupacio.user_activity_manager.bootstrap;

import com.github.javafaker.Faker;
import com.hackathon.saloocupacio.user_activity_manager.model.Activity;
import com.hackathon.saloocupacio.user_activity_manager.model.User;
import com.hackathon.saloocupacio.user_activity_manager.repository.ActivityRepository;
import com.hackathon.saloocupacio.user_activity_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;
    private Faker faker = new Faker();
    private String[] activityNames = {
            "Nature Walk",
            "Painting Workshop",
            "International Cooking Classes",
            "Reading Marathon",
            "Photography Course",
            "Yoga Training",
            "Mountain Excursion",
            "Chess Tournament",
            "Outdoor Movie Night",
            "Technology Conference",
            "Creative Writing Workshop",
            "Beach Cleanup Day",
            "Costume Party",
            "Bike Ride",
            "Salsa Dance Class",
            "Local Music Festival",
            "Volunteering Day",
            "Gardening Workshop",
            "Cooking Competition",
            "Meditation Retreat",
            "Storytelling Session",
            "Night Hike",
            "Martial Arts Training",
            "Pottery Class",
            "Craft Workshop",
            "Mental Health Talk Series",
            "Family Game Day",
            "Night Photography Workshop",
            "Food Tour",
            "Video Game Tournament"
    };

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (activityRepository.count() == 0 && userRepository.count() == 0) {
            createUsers(10);
            createActivities(10);
            assignActivitiesToUsers();
        }
    }

    private void createUsers(int numberOfUsers) {
        for (int i = 0; i < numberOfUsers; i++) {
            User user = new User();
            user.setName(faker.name().firstName());
            user.setSurname(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            user.setAge(faker.number().numberBetween(18, 100));
            userRepository.save(user);
        }
    }

    private void createActivities(int numberOfActivities) {
        for (int i = 0; i < numberOfActivities; i++) {
            Activity activity = new Activity();
            String activityName = activityNames[faker.random().nextInt(activityNames.length)];
            activity.setName(activityName);
            activity.setDescription(faker.lorem().sentence(20));
            activity.setMaxCapacity(faker.number().numberBetween(1, 50));
            activityRepository.save(activity);
        }
    }

    private void assignActivitiesToUsers() {
        List<User> users = userRepository.findAll();
        List<Activity> activities = activityRepository.findAll();
        for (User user : users) {
            for (Activity activity : activities) {
                if (faker.random().nextBoolean()) {
                    user.getActivities().add(activity);
                    userRepository.save(user);
                }
            }
        }
    }
}