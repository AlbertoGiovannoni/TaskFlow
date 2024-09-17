package com.example.taskflow.DTOs.FieldDefinition;

import java.util.ArrayList;

public class AssigneeDefinitionDTO extends FieldDefinitionDTO{
    ArrayList<String> possibleAssigneeUserIds;

    public ArrayList<String> getUserIds() {
        return possibleAssigneeUserIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.possibleAssigneeUserIds = userIds;
    }
}
