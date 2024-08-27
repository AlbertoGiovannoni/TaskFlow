package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class AssigneeBuilder extends FieldBuilder{
    private ArrayList<User>  assignees;

    AssigneeBuilder(FieldType type) {
        super(type);
        this.assignees = new ArrayList<>();
    }

    @Override
    public AssigneeBuilder setUsers(ArrayList<User> users){
        this.assignees = users;
        return this.self();
    }

    @Override
    protected AssigneeBuilder self() {
        return this;
    }

    @Override
    public Field build() {
        if (this.assignees == null){
            throw new IllegalAccessError("possibleAssignees are null");
        }
        return new Assignee( this.fieldDefinition, assignees);
    }
}