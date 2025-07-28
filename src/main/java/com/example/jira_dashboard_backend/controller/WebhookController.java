package com.example.jira_dashboard_backend.controller;

import com.example.jira_dashboard_backend.model.Task;
import com.example.jira_dashboard_backend.repository.TaskRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class WebhookController {

    private final TaskRepository taskRepository;

    public WebhookController(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @PostMapping("/api/github/webhook")
    public ResponseEntity<String> handleGitHubWebhook(@RequestHeader("X-Github-Event") String githubEvent, @RequestBody String payload){
        if(!"push".equals(githubEvent)){
            return ResponseEntity.ok("Event Ignored: " + githubEvent);
        }

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(payload);
            JsonNode commits = rootNode.path("commits");
            for(JsonNode commit : commits){
                String commitMessage = commit.path("message").asText();
                Pattern pattern = Pattern.compile("task #(\\d+)");
                Matcher matcher = pattern.matcher(commitMessage);

                if(matcher.find()){
                    long taskId = Long.parseLong(matcher.group(1));
                    Optional<Task> taskOptional = taskRepository.findByTaskId(taskId);
                    if(taskOptional.isPresent()){
                        Task task = taskOptional.get();
                        task.setStatus("Done");
                        taskRepository.save(task);
                        System.out.println("Task #" + taskId+ " marked as Done due to commit.");
                    } else {
                        System.out.println("Task #" + taskId + " not found. Skipping update.");
                    }
                }
            }

            return ResponseEntity.ok("Webhook Processed successfully");
        }catch (IOException e){
            System.err.println("Error processing webhook payload: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error processing payload");
        }
    }
}
