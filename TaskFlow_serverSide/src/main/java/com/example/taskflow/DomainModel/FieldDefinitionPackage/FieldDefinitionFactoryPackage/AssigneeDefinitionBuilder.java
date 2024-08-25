package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class AssigneeDefinitionBuilder extends FieldDefinitionBuilder<AssigneeDefinition, AssigneeDefinitionBuilder> implements SpecificBuilder<AssigneeDefinitionBuilder, ArrayList<User>>{
    private ArrayList<User>  possibleAssignees;

    @Override
    public AssigneeDefinitionBuilder addSpecificField(ArrayList<User> possibleAssignees) {
        this.possibleAssignees = possibleAssignees;
        return this;
    }

    @Override
    protected AssigneeDefinitionBuilder self() {
        return this;
    }

    @Override
    public AssigneeDefinition build() {
        return new AssigneeDefinition(name, FieldType.ASSIGNEE, possibleAssignees);
    }
}
