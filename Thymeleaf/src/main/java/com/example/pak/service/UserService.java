package com.example.pak.service;

import com.example.pak.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, String> tokens = new HashMap<>(); 
    public UserService() {
        users.put("admin", new User("admin","admin123","Pentadbir"));
        users.put("user", new User("user","user123","Ahli"));
    }

    public Optional<User> authenticate(String username, String password) {
        User u = users.get(username);
        if (u != null && u.getPassword().equals(password)) return Optional.of(u);
        return Optional.empty();
    }

    public User createUser(String username, String password, String role) {
        User u = new User(username, password, role);
        users.put(username, u);
        return u;
    }

    public String createTokenFor(String username) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, username);
        return token;
    }

    public Optional<User> findByToken(String token) {
        String username = tokens.get(token);
        if (username == null) return Optional.empty();
        return Optional.ofNullable(users.get(username));
    }

    public void revokeToken(String token){
        tokens.remove(token);
    }
}
