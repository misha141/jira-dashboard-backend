package com.example.jira_dashboard_backend.repository;

import com.example.jira_dashboard_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
}
