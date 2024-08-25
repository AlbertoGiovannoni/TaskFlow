package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;

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
