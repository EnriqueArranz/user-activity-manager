package com.hackathon.saloocupacio.user_activity_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.saloocupacio.user_activity_manager.model.Activity;
import com.hackathon.saloocupacio.user_activity_manager.model.User;
import com.hackathon.saloocupacio.user_activity_manager.service.ActivityService;
import com.hackathon.saloocupacio.user_activity_manager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/appActivities")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;

    @PostMapping("/activity")
    public String createActivity(@ModelAttribute Activity activity) {
        activityService.createActivity(activity);
        return "redirect:/appActivities/activities";
    }

    @GetMapping("/activities")
    public String getAllActivities(Model model) {
        List<Activity> activities = activityService.getAllActivities();
        model.addAttribute("activities", activities);
        return "activities";
    }

    @DeleteMapping("/activities/{id}")
    public String deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
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
    public String updateActivity(@PathVariable Long id, @ModelAttribute Activity activity) {
        activityService.updateActivity(id, activity);
        return "redirect:/appActivities/activities";
    }

    @GetMapping("/activities/signup/{activityId}")
    public String signUpToActivity(@PathVariable Long activityId, Model model) {
        Activity activity = activityService.findById(activityId);
        List<User> users = userService.getAllUsers();
        model.addAttribute("activityId", activityId);
        model.addAttribute("activity", activity);
        model.addAttribute("users", users);
        return "activityDetails";
    }

    @PostMapping("activities/signup/{activityId}")
    public String signUpUserToActivity(@PathVariable Long activityId, @RequestParam Long userId) {
        activityService.signUpUserInActivity(activityId, userId);
        return "redirect:/appActivities/activities";
    }
}