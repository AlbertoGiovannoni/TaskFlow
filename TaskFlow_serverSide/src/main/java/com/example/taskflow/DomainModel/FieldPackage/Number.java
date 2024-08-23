package com.example.taskflow.DomainModel.FieldPackage;

public class Number extends Field<Integer>{

    public Number(Integer value) {
        super(value);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void validateValue(Integer value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateValue'");
    }
    
}
