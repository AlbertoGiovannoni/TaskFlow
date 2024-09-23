package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class AssigneeDefinitionBuilder extends FieldDefinitionBuilder{
    private ArrayList<User>  possibleAssignees;

    public AssigneeDefinitionBuilder() {
        super(FieldType.ASSIGNEE);
    }

    @Override
    public AssigneeDefinition build() {
        if (this.possibleAssignees == null){
            throw new IllegalAccessError("possibleAssignees are null");
        }

        AssigneeDefinition assigneeDefinition = new AssigneeDefinition(UUID.randomUUID().toString());

        assigneeDefinition.setName(this.name);
        assigneeDefinition.setType(this.type);
        assigneeDefinition.setPossibleAssigneeUsers(this.possibleAssignees);

        return assigneeDefinition;
    }

    @Override
    public AssigneeDefinitionBuilder reset() {
        this.possibleAssignees.clear();
        return this;
    }

    public AssigneeDefinitionBuilder setUsers(ArrayList<User> users){
        this.possibleAssignees = users;
        return this;
    }

}
