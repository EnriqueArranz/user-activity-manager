package com.hackathon.saloocupacio.user_activity_manager.controller;

import com.hackathon.saloocupacio.user_activity_manager.model.Activity;
import com.hackathon.saloocupacio.user_activity_manager.model.SignUpResponse;
import com.hackathon.saloocupacio.user_activity_manager.model.User;
import com.hackathon.saloocupacio.user_activity_manager.service.ActivityService;
import com.hackathon.saloocupacio.user_activity_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/appActivities")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;

    @PostMapping("/activity")
    public String createActivity(@ModelAttribute Activity activity, RedirectAttributes redirectAttributes) {
        activityService.createActivity(activity);
        redirectAttributes.addFlashAttribute("addSuccess", true);
        return "redirect:/appActivities/activities";
    }

    @GetMapping("/activities")
    public String getAllActivities(Model model) {
        List<Activity> activities = activityService.getAllActivities();
        model.addAttribute("activities", activities);
        return "activities";
    }

    @PostMapping("/activities/{id}")
    public String deleteActivity(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        activityService.deleteActivity(id);
        redirectAttributes.addFlashAttribute("deleteSuccess", true);
        return "redirect:/appActivities/activities";
    }

    @PostMapping("/activities/import")
    public String importActivities(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            File tempFile = File.createTempFile("import", ".json");
            file.transferTo(tempFile);
            List<Activity> importedActivities = activityService.importActivities(tempFile);
            tempFile.delete();
            redirectAttributes.addFlashAttribute("importSuccess", true);
            redirectAttributes.addFlashAttribute("importedCount", importedActivities.size());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("importError", "Failed to import activities: " + e.getMessage());
        }
        return "redirect:/appActivities/activities";
    }

    @GetMapping("/activities/export")
    public ResponseEntity<byte[]> exportActivities() throws IOException {
        File tempFile = File.createTempFile("activities", ".json");
        activityService.exportActivities(tempFile);
        byte[] fileContent = Files.readAllBytes(tempFile.toPath());
        tempFile.delete();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "activities.json");
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    @GetMapping("/activities/{id}")
    public String editActivity(@PathVariable Long id, Model model) {
        Activity activity = activityService.getActivityById(id);
        model.addAttribute("activity", activity);
        return "updateActivity";
    }

    @PutMapping("/activities/{id}")
    public String updateActivity(@PathVariable Long id, @ModelAttribute Activity activity, RedirectAttributes redirectAttributes) {
        activityService.updateActivity(id, activity);
        redirectAttributes.addFlashAttribute("editSuccess", true);
        return "redirect:/appActivities/activities";
    }

    @GetMapping("/activities/signup/{activityId}")
    public String signUpToActivity(@PathVariable Long activityId, Model model) {
        Activity activity = activityService.findById(activityId);
        List<User> users = userService.getAllUsers();
        List<User> usersNotSignedUp = users.stream()
                .filter(user -> !activity.getUsers().contains(user))
                .collect(Collectors.toList());
        model.addAttribute("activityId", activityId);
        model.addAttribute("activity", activity);
        model.addAttribute("users", users);
        model.addAttribute("usersNotSignedUp", usersNotSignedUp);
        return "activityDetails";
    }

    @PostMapping("activities/signup/{activityId}")
    public String signUpUserToActivity(@PathVariable Long activityId, @RequestParam Long userId, RedirectAttributes redirectAttrs) {
        SignUpResponse response = activityService.signUpUserInActivity(activityId, userId);
        if (response.isSuccess()) {
            redirectAttrs.addFlashAttribute("message", response.getMessage());
            redirectAttrs.addFlashAttribute("messageType", "success");
        } else {
            redirectAttrs.addFlashAttribute("message", response.getMessage());
            redirectAttrs.addFlashAttribute("messageType", "failure");
        }
        return "redirect:/appActivities/activities/signup/" + activityId;
    }
    @GetMapping("/activities/signup/{activityId}/users")
    public String getUsersInActivity(@PathVariable Long activityId, Model model) {
        Optional<List<User>> users = activityService.getUsersInActivity(activityId);
        Activity activity = activityService.getActivityById(activityId);
        model.addAttribute("activity", activity);
        if (users.isPresent()) {
            model.addAttribute("users", users.get());
        } else {
            model.addAttribute("users", Collections.emptyList());
            model.addAttribute("noUsers", true);
        }
        return "usersInActivity";
    }


}