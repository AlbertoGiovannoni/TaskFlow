package com.example.taskflow.DTOs.FieldDefinition;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class AssigneeDefinitionDTO extends FieldDefinitionDTO{
    private ArrayList<String> assigneeIds;

    public ArrayList<String> getAssigneeIds() {
        return assigneeIds;
    }

    public void setAssigneeIds(ArrayList<String> assigneeIds) {
        this.assigneeIds = assigneeIds;
    }
}
