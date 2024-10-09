package com.unifi.taskflow.domainModel.fields.fieldBuilders;
import java.util.ArrayList;

import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Assignee;
import com.unifi.taskflow.domainModel.fields.Field;

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

        Assignee assignee = EntityFactory.getAssignee();
        
        assignee.setFieldDefinition(this.fieldDefinition);
        assignee.setUsers(this.assignees);

        return assignee;
    }
}