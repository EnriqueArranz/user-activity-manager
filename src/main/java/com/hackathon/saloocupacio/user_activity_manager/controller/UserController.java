package com.hackathon.saloocupacio.user_activity_manager.controller;

import com.hackathon.saloocupacio.user_activity_manager.model.Activity;
import com.hackathon.saloocupacio.user_activity_manager.model.User;
import com.hackathon.saloocupacio.user_activity_manager.repository.ActivityRepository;
import com.hackathon.saloocupacio.user_activity_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/appActivities")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityRepository activityRepository;

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user , RedirectAttributes redirectAttributes) {
        if(userService.existsUserWithEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "User with the same email already exists");
            return "redirect:/appActivities/users";
        }
        userService.createUser(user);
        redirectAttributes.addFlashAttribute("addSuccess", true);
        return "redirect:/appActivities/users";
    }


    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/{id}")
    public String editPanelUser(@PathVariable Long id, Model model) {
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