package com.example.dashboard.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pak.model.User;
import com.example.pak.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private final UserService userService;
    public AuthRestController(UserService userService) { this.userService = userService; }

    @PostMapping("/login")
    public ResponseEntity<?> apiLogin(@RequestBody Map<String,String> body){
        String username = body.get("username");
        String password = body.get("password");
        var opt = userService.authenticate(username, password);
        if (opt.isPresent()){
            User u = opt.get();
            String token = userService.createTokenFor(username);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "token", token,
                "user", Map.of("username", u.getUsername(), "role", u.getRole())
            ));
        } else {
            return ResponseEntity.ok(Map.of("success", false, "message", "Invalid credentials"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestHeader(value="Authorization", required=false) String auth){
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.ok(Map.of("success", false, "message", "Missing token"));
        }
        String token = auth.substring(7);
        var opt = userService.findByToken(token);
        if (opt.isPresent()){
            User u = opt.get();
            return ResponseEntity.ok(Map.of("success", true, "user", Map.of("username", u.getUsername(), "role", u.getRole())));
        } else {
            return ResponseEntity.ok(Map.of("success", false));
        }
    }
}
