package com.example.jira_dashboard_backend.controller;

import com.example.jira_dashboard_backend.model.User;
import com.example.jira_dashboard_backend.repository.UserRepository;
import com.example.jira_dashboard_backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser){
        User user = userRepository.findByEmail(loginUser.getEmail());
        if(user != null && user.getPassword().equals(loginUser.getPassword())){
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok().body(Map.of("token",token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
