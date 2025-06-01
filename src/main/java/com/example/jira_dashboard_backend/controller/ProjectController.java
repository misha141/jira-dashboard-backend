package com.example.jira_dashboard_backend.controller;

import com.example.jira_dashboard_backend.model.Project;
import com.example.jira_dashboard_backend.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Project> getProjectById(@PathVariable String id){
        return projectRepository.findById(id);
    }

    @PostMapping
    public Project createproject(@RequestBody Project project){
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
