package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Assignee extends Field{

    ArrayList<User> assignees;

    public Assignee(FieldDefinition fieldDefinition, ArrayList<User> assignees) {
        super(fieldDefinition);
        this.assignees = assignees;
    }
}
