package com.hackathon.saloocupacio.user_activity_manager.controller;

import com.hackathon.saloocupacio.user_activity_manager.exception.ResourceNotFoundException;
import com.hackathon.saloocupacio.user_activity_manager.model.Activity;
import com.hackathon.saloocupacio.user_activity_manager.model.User;
import com.hackathon.saloocupacio.user_activity_manager.repository.ActivityRepository;
import com.hackathon.saloocupacio.user_activity_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/appActivities")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityRepository activityRepository;

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/appActivities/users";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "updateUser";
    }

    @PutMapping("users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/appActivities/users";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/appActivities/users";
    }


//    @GetMapping("/signup")
//    public String showSignUpForm(Model model) {
//        // Cargamos todas las actividades disponibles
//        List<Activity> activities = activityRepository.findAll();
//        model.addAttribute("activities", activities);
//        return "signup"; // Renderiza el formulario signup.html
//    }
//
//    @PostMapping("/signup")
//    public String signUpToActivity(
//            @RequestParam String email,
//            @RequestParam Long activityId) {
//        System.out.println("email: " + email);
//        System.out.println("activityId: " + activityId);
//        userService.signUpToActivity(email, activityId);
//        return "redirect:/signup?success";
//    }

    @GetMapping("/signup/{activityId}/success")
    public String showSuccessPage() {
        return "signupSuccess";
    }

    @GetMapping("/signup/{activityId}")
    public String signUpToActivity(@PathVariable Long activityId, Model model) {
        model.addAttribute("activityId", activityId);
        return "signupUser";
    }


    @PostMapping("/signup/{activityId}")
    public String signUpUserToActivity(@PathVariable Long activityId, @RequestParam String email) {
        userService.signUpUserToActivity(email, activityId);
        return "redirect:/appActivities/signup/" + activityId + "/success";
    }

}