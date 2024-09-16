package com.example.taskflow.DTOs.FieldDefinition;

import java.util.ArrayList;

public class AssigneeDefinitionDTO extends FieldDefinitionDTO{
    ArrayList<String> userIds;

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }
}
