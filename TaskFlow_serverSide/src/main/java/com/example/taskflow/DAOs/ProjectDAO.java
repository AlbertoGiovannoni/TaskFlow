package com.example.taskflow.DAOs;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskflow.DomainModel.Project;

public interface ProjectDAO extends MongoRepository<Project, String>, CustomProjectDAO{
}
