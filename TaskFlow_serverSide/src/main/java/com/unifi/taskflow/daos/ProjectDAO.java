package com.unifi.taskflow.daos;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.unifi.taskflow.domainModel.Project;

public interface ProjectDAO extends MongoRepository<Project, String>, CustomProjectDAO{
}
