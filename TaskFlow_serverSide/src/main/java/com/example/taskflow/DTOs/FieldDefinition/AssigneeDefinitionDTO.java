package com.example.taskflow.DTOs.FieldDefinition;

import java.util.ArrayList;

public class AssigneeDefinitionDTO extends FieldDefinitionDTO{
    private ArrayList<String> possibleAssigneeUserIds;

    public ArrayList<String> getPossibleAssigneeUserIds() {
        return possibleAssigneeUserIds;
    }

    public void setPossibleAssigneeUserIds(ArrayList<String> possibleAssigneeUserIds) {
        this.possibleAssigneeUserIds = possibleAssigneeUserIds;
    }
}
