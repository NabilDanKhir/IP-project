package com.example.pak.controller;

import com.example.pak.model.User;
import com.example.pak.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MvcController {
    private final UserService userService;
    public MvcController(UserService userService){ this.userService = userService; }

    @GetMapping("/")
    public String getLoginPage(Model model){
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              Model model){
        var opt = userService.authenticate(username, password);
        if (opt.isPresent()){
            User u = opt.get();
            session.setAttribute("user", u);
            // redirect to menu
            return "redirect:/menu";
        } else {
            model.addAttribute("message", "Nama pengguna atau kata laluan salah");
            model.addAttribute("error", true);
            return "login";
        }
    }

    @GetMapping("/signup")
    public String signupForm() { return "signup"; }

    @PostMapping("/signup")
    public String handleSignup(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(required = false) String role,
                               Model model,
                               HttpSession session){
        if (username==null || username.isBlank()){
            model.addAttribute("message","Sila masukkan nama pengguna");
            model.addAttribute("error", true);
            return "signup";
        }
        if (userService.authenticate(username,password).isPresent()){
            model.addAttribute("message","Nama pengguna sudah wujud");
            model.addAttribute("error", true);
            return "signup";
        }
        userService.createUser(username, password, (role==null ? "Ahli" : role));
        model.addAttribute("message","Daftar berjaya. Sila log masuk.");
        model.addAttribute("error", false);
        return "login";
    }

    @GetMapping("/menu")
    public String menuPage(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/";
        model.addAttribute("user", u);
        return "menu";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
