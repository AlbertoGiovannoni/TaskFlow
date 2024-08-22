package com.example.taskflow.DAOs;
import com.example.taskflow.DomainModel.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrganizationDAO extends MongoRepository<Organization, String> {
}