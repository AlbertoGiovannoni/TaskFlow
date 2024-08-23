package com.example.taskflow.DomainModel.FieldPackage;

import java.time.LocalDateTime;

public class Date extends Field<LocalDateTime>{

    public Date(LocalDateTime value) {
        super(value);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void validateValue(LocalDateTime value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateValue'");
    }
    
}
