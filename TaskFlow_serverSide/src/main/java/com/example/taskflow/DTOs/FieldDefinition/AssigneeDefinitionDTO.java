package com.example.taskflow.DTOs.FieldDefinition;

import java.util.ArrayList;

import jakarta.validation.constraints.NotNull;

public class AssigneeDefinitionDTO extends FieldDefinitionDTO{
    @NotNull
    private ArrayList<String> assigneeIds;
    @NotNull
    private String organizationId;

    public ArrayList<String> getAssigneeIds() {
        return assigneeIds;
    }

    public void setAssigneeIds(ArrayList<String> assigneeIds) {
        this.assigneeIds = assigneeIds;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
