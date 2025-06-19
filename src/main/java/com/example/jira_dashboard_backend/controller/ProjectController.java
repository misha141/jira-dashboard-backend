package com.example.jira_dashboard_backend.controller;

import com.example.jira_dashboard_backend.model.Project;
import com.example.jira_dashboard_backend.repository.ProjectRepository;
import com.example.jira_dashboard_backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> updateProject(@PathVariable String id, @RequestBody Project updatedProject, HttpServletRequest request){
        Optional<Project> optionalProject = projectRepository.findById(id);
        if(optionalProject.isPresent()){
            Project existingProject = optionalProject.get();
            String userEmail = jwtUtil.extractEmail(request.getHeader("Authorization").substring(7));

            if(existingProject.getCreatedBy().equals(userEmail)){
                existingProject.setName(updatedProject.getName());
                existingProject.setDescription(updatedProject.getDescription());
                return ResponseEntity.ok(projectRepository.save(existingProject));
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this project.");
            }
           }else{
            updatedProject.setId(id);
            updatedProject.setCreatedBy(jwtUtil.extractEmail(request.getHeader("Authorization").substring(7)));
            return ResponseEntity.status(HttpStatus.CREATED).body(projectRepository.save(updatedProject));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id, HttpServletRequest request) {
        Optional<Project> projectOpt = projectRepository.findById(id);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            String userEmail = jwtUtil.extractEmail(request.getHeader("Authorization").substring(7));

            if (project.getCreatedBy().equals(userEmail)) {
                projectRepository.deleteById(id);
                return ResponseEntity.ok("Project deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this project.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
        }
    }
}
