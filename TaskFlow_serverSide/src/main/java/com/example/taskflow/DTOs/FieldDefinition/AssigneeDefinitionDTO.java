package com.example.taskflow.DTOs.FieldDefinition;

import java.util.ArrayList;

public class AssigneeDefinitionDTO extends FieldDefinitionDTO{
    private ArrayList<String> assigneeIds;

    public ArrayList<String> getAssigneeIds() {
        return assigneeIds;
    }

    public void setAssigneeIds(ArrayList<String> assigneeIds) {
        this.assigneeIds = assigneeIds;
    }
}
