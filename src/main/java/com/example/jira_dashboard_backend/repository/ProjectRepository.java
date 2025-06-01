package com.example.jira_dashboard_backend.repository;

import com.example.jira_dashboard_backend.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
}
