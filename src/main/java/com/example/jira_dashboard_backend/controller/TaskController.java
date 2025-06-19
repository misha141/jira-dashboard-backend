package com.example.jira_dashboard_backend.controller;

import com.example.jira_dashboard_backend.model.Task;
import com.example.jira_dashboard_backend.repository.TaskRepository;
import com.example.jira_dashboard_backend.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;

    public TaskController(TaskRepository taskRepository, JwtUtil jwtUtil){
        this.taskRepository = taskRepository;
        this.jwtUtil = jwtUtil;
    }
    @GetMapping
    public List<Task> getAllTasks(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String userEmail = jwtUtil.extractEmail(token);
        return taskRepository.findByCreatedBy(userEmail);
    }
    @GetMapping("/project/{projectId}")
    public List<Task> getTaskByProject(@PathVariable String projectId){
        return taskRepository.findByProjectId(projectId);
    }

    @GetMapping("/{id}")
    public Optional<Task> getTasksById(@PathVariable String id){
        return taskRepository.findById(id);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String userEmail = jwtUtil.extractEmail(token);
        task.setCreatedBy(userEmail);
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable String id, @RequestBody Task updateTask){
        Optional<Task> existingTaskOpt = taskRepository.findById(id);

        if(existingTaskOpt.isPresent()){
            Task existingTask = existingTaskOpt.get();
            existingTask.setTitle(updateTask.getTitle());
            existingTask.setDescription(updateTask.getDescription());
            existingTask.setStatus(updateTask.getStatus());
            return taskRepository.save(existingTask);
        }else{
            updateTask.setId(id);
            return taskRepository.save(updateTask);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id){
        taskRepository.deleteById(id);
    }

}
