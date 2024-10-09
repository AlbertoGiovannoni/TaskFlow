package com.unifi.taskflow.daos;

import com.unifi.taskflow.domainModel.Project;

public interface CustomProjectDAO {
    void removeFieldDefinitionFromProject(String fieldDefinitionId);
    Project findProjectByFieldDefinition(String fieldDefinitionId);
    Project findProjectByActivity(String activityId);
}
