package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class AssigneeDefinitionBuilder extends FieldDefinitionBuilder<AssigneeDefinition, ArrayList<User>, AssigneeDefinitionBuilder>{
    private ArrayList<User>  possibleAssignees;

    AssigneeDefinitionBuilder(FieldType type) {
        super(type);
    }

    @Override
    public AssigneeDefinitionBuilder addSpecificField(ArrayList<User> possibleAssignees) {
        this.possibleAssignees = possibleAssignees;
        return this.self();
    }

    @Override
    protected AssigneeDefinitionBuilder self() {
        return this;
    }

    @Override
    public AssigneeDefinition build() {
        if (this.possibleAssignees == null){
            throw new IllegalAccessError("possibleAssignees are null");
        }
        return new AssigneeDefinition(name, this.type, possibleAssignees);
    }
}
