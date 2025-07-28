package com.example.jira_dashboard_backend.repository;

import com.example.jira_dashboard_backend.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByProjectId(String projectId);
    List<Task> findByCreatedBy(String createdBy);

    Optional<Task> findByTaskId(long taskId);

}
