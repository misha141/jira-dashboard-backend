package com.example.jira_dashboard_backend.controller;

import com.example.jira_dashboard_backend.model.Project;
import com.example.jira_dashboard_backend.repository.ProjectRepository;
import com.example.jira_dashboard_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final JwtUtil jwtUtil;

    public ProjectController(ProjectRepository projectRepository, JwtUtil jwtUtil){
        this.projectRepository = projectRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Project> getAllProjects(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String userEmail = jwtUtil.extractEmail(token);
        return projectRepository.findByCreatedBy(userEmail);
    }

    @GetMapping("/{id}")
    public Optional<Project> getProjectById(@PathVariable String id){
        return projectRepository.findById(id);
    }

    @PostMapping
    public Project createproject(@RequestBody Project project, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String userEmail = jwtUtil.extractEmail(token);
        project.setCreatedBy(userEmail);
        return projectRepository.save(project);
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable String id, @RequestBody Project updatedProject){
        Optional<Project> optionalProject = projectRepository.findById(id);
        if(optionalProject.isPresent()){
            Project project = optionalProject.get();
            project.setName(updatedProject.getName());
            project.setDescription(updatedProject.getDescription());
            return projectRepository.save(project);
        }else{
            updatedProject.setId(id);
            return projectRepository.save(updatedProject);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable String id){
        projectRepository.deleteById(id);
    }
}
