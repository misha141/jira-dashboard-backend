package com.example.jira_dashboard_backend.controller;

import com.example.jira_dashboard_backend.Service.SequenceGeneratorService;
import com.example.jira_dashboard_backend.model.Task;
import com.example.jira_dashboard_backend.repository.TaskRepository;
import com.example.jira_dashboard_backend.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final JwtUtil jwtUtil;

    public TaskController(TaskRepository taskRepository, SequenceGeneratorService sequenceGeneratorService, JwtUtil jwtUtil){
        this.taskRepository = taskRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
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
        task.setTaskId((sequenceGeneratorService.generateSequence("tasks_sequence")));
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody Task updateTask, HttpServletRequest request){
        Optional<Task> existingTaskOpt = taskRepository.findById(id);

        if(existingTaskOpt.isPresent()){
            Task existingTask = existingTaskOpt.get();
            String userEmail = jwtUtil.extractEmail(request.getHeader("Authorization").substring(7));

            if(existingTask.getCreatedBy().equals(userEmail)){
                existingTask.setTitle(updateTask.getTitle());
                existingTask.setDescription(updateTask.getDescription());
                existingTask.setStatus(updateTask.getStatus());
                return ResponseEntity.ok(taskRepository.save(existingTask));
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this task.");
            }

        }else{
            updateTask.setId(id);
            updateTask.setCreatedBy(jwtUtil.extractEmail(request.getHeader("Authorization").substring(7)));
            return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(updateTask));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id, HttpServletRequest request){
        Optional<Task> taskOpt = taskRepository.findById(id);

        if(taskOpt.isPresent()){
            Task task = taskOpt.get();
            String userEmail = jwtUtil.extractEmail(request.getHeader("Authorization").substring(7));

            if(task.getCreatedBy().equals(userEmail)){
                taskRepository.deleteById(id);
                return ResponseEntity.ok("Task deleted successfully.");

            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this task.");
            }

        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }

    }

}
