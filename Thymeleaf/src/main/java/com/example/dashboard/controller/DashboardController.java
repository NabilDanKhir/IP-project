package com.example.dashboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Admin Dashboard");

        List<Map<String, Object>> modules = List.of(
            Map.of("key","account","label", "Account Management", "desc", "Edit and Delete accounts", "link", "/accounts"),
            Map.of("key","profile","label", "Member Profile", "desc", "View and edit member profiles", "link", "/members"),
            Map.of("key","meeting","label", "Meeting Management", "desc", "Create meetings & upload minutes", "link", "/meetings"),
            Map.of("key","attendance","label", "Attendance Management", "desc", "Manage and check attendance", "link", "/attendance")
        );
        model.addAttribute("modules", modules);

        List<Map<String, Object>> recent = List.of(
            Map.of("id", 101, "name", "Alice", "status", "Active"),
            Map.of("id", 102, "name", "Bob", "status", "Pending"),
            Map.of("id", 103, "name", "Carol", "status", "Active")
        );
        model.addAttribute("recent", recent);

        return "dashboard";
    }
}
