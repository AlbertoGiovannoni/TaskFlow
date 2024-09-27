package com.example.taskflow.DAOs;

import com.example.taskflow.DomainModel.Project;

public interface CustomProjectDAO {
    void removeFieldDefinitionFromProject(String fieldDefinitionId);
    Project findProjectByFieldDefinition(String fieldDefinitionId);
}
