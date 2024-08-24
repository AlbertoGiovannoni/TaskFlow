package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Assignee extends Field<ArrayList<User>>{

    public Assignee(ArrayList<User> value, FieldDefinition fieldDefinition) {
        super(value, fieldDefinition);
        //TODO Auto-generated constructor stub
    }    
}
