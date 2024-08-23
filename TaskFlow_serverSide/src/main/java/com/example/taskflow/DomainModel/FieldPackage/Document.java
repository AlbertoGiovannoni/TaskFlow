package com.example.taskflow.DomainModel.FieldPackage;
import org.bson.types.ObjectId;

public class Document extends Field<ObjectId>{ //TODO non so se il tipo ObjectId va bene per quello che dobbiamo fare

    public Document(ObjectId value) {
        super(value);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void validateValue(ObjectId value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateValue'");
    }
    
}
