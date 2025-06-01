package com.example.jira_dashboard_backend.controller;

import com.example.jira_dashboard_backend.model.Project;
import com.example.jira_dashboard_backend.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository repository;

    public ProjectController(ProjectRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Project> getAllProjects(){
        return repository.findAll();
    }

    @PostMapping
    public Project createproject(@RequestBody Project project){
        return repository.save(project);
    }
}
