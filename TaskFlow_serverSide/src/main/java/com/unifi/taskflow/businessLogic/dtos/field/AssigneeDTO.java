package com.unifi.taskflow.businessLogic.dtos.field;

import java.util.ArrayList;

public class AssigneeDTO extends FieldDTO {
    private ArrayList<String> userIds;

    public ArrayList<String> getUserIds() {
        return userIds;
    }
    
    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }
}
