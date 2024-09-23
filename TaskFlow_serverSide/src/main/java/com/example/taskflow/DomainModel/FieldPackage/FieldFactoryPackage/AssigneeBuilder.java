package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;
import java.util.ArrayList;

import java.util.UUID;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class AssigneeBuilder extends FieldBuilder{
    private ArrayList<User>  assignees;

    public AssigneeBuilder(FieldDefinition fieldDefinition) {
        super(fieldDefinition);
        this.assignees = new ArrayList<>();
    }

    public FieldBuilder addAssignees(ArrayList<User> values){
        for (User value : values){
            this.addAssignee(value);
        }
        return this;
    }

    public FieldBuilder addAssignee(User value){
        if (value != null){
            this.assignees.add(value);
        }
        return this;
    }

    @Override
    public Field build() {
        if (this.assignees == null){
            throw new IllegalAccessError("possibleAssignees are null");
        }

        Assignee assignee = new Assignee(UUID.randomUUID().toString());
        
        assignee.setFieldDefinition(this.fieldDefinition);
        assignee.setUsers(this.assignees);

        return assignee;
    }
}