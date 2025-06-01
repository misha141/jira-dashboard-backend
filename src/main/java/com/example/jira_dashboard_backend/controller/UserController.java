package com.example.jira_dashboard_backend.controller;

import com.example.jira_dashboard_backend.model.User;
import com.example.jira_dashboard_backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginUser){
        User user = userRepository.findByEmail(loginUser.getEmail());
        if(user != null && user.getPassword().equals(loginUser.getPassword())){
            return "Login Successful!";
        }
        return "Invalid Credentials!";
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
