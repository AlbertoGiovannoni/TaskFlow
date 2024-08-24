package com.example.taskflow.DomainModel.FieldPackage;
import org.bson.types.ObjectId;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Document extends Field<ObjectId>{ //TODO non so se il tipo ObjectId va bene per quello che dobbiamo fare

    private String name;
    private String fileType;
    
    public Document(ObjectId value, FieldDefinition fieldDefinition) {
        super(value, fieldDefinition);
        //TODO Auto-generated constructor stub
    }

   
}
