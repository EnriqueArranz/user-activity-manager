package com.hackathon.saloocupacio.user_activity_manager.controller;

import com.hackathon.saloocupacio.user_activity_manager.service.ActivityService;
import com.hackathon.saloocupacio.user_activity_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/activities")
    public String activities(Model model) {
        model.addAttribute("activities", activityService.getAllActivities());
        return "activities";
    }
}