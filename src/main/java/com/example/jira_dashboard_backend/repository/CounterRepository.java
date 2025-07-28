package com.example.jira_dashboard_backend.repository;

import com.example.jira_dashboard_backend.model.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CounterRepository extends MongoRepository<Counter, String> {
}
