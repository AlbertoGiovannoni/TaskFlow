package com.example.taskflow.DTOs.Field;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class AssigneeDTO extends FieldDTO {
    private ArrayList<String> userIds;

    public AssigneeDTO() {
        this.setType(FieldType.ASSIGNEE);  // Set the type explicitly in constructor
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }
    
    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }
}
