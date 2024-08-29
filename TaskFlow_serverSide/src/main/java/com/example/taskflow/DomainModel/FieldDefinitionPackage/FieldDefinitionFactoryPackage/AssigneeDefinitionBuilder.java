package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class AssigneeDefinitionBuilder extends FieldDefinitionBuilder{
    private ArrayList<User>  possibleAssignees;

    AssigneeDefinitionBuilder(FieldType type) {
        super(type);
        this.possibleAssignees = new ArrayList<>();
    }

    @Override
    public AssigneeDefinition build() {
        if (this.possibleAssignees == null){
            throw new IllegalAccessError("possibleAssignees are null");
        }
        return new AssigneeDefinition(name, this.type, possibleAssignees);
    }

    @Override
    public FieldDefinitionBuilder addParameters(ArrayList<?> values) {
        for (Object value : values){
            this.addParameter(value);
        }
        return this;
    }

    @Override
    public FieldDefinitionBuilder addParameter(Object value) {
        if (value != null){
            if (value instanceof User){
                this.possibleAssignees.add((User)value);
            }
        }
        return this;
    }

    @Override
    public void reset() {
        this.possibleAssignees.clear();
    }
}
