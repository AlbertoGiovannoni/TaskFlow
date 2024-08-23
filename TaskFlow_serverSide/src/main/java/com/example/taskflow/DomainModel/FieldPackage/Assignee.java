package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;

public class Assignee extends Field<ArrayList<User>>{

    public Assignee(ArrayList<User> value) {
        super(value);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void validateValue(ArrayList<User> value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateValue'");
    }
    
}
