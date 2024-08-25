package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class AssegneeDefinitionFactory extends FieldDefinitionFactory<AssigneeDefinition, AssegneeDefinitionFactory> {
    private ArrayList<User>  possibleAssignees;

    public AssegneeDefinitionFactory addSpecificField(ArrayList<User> possibleAssignees) {
        this.possibleAssignees = possibleAssignees;
        return this;
    }

    @Override
    protected AssegneeDefinitionFactory self() {
        return this;
    }

    @Override
    public AssigneeDefinition build() {
        return new AssigneeDefinition(name, FieldType.ASSIGNEE, possibleAssignees);
    }
}
