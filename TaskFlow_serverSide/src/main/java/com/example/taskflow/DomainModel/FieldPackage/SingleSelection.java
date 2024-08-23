package com.example.taskflow.DomainModel.FieldPackage;

public class SingleSelection extends Field<String>{ // TODO non so se String va bene come tipo per questo caso

    public SingleSelection(String value) {
        super(value);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void validateValue(String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateValue'");
    }
    
}
