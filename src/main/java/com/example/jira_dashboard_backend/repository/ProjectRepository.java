package com.example.jira_dashboard_backend.repository;

import com.example.jira_dashboard_backend.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findByCreatedBy(String createdBy);
}
