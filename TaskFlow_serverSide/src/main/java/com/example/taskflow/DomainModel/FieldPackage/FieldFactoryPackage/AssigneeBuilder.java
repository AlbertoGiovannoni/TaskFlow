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
    public FieldBuilder addParameters(ArrayList<?> values){
        for (Object value : values){
            this.addParameter(value);
        }
        return this;
    }

    @Override
    public FieldBuilder addParameter(Object value){
        if (value != null){
            if (value instanceof User){
                this.assignees.add((User)value);
            }
        }
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