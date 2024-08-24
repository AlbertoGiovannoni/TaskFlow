package com.example.taskflow.DomainModel.FieldPackage;
import org.bson.types.ObjectId;

public class Document extends Field<ObjectId>{ //TODO non so se il tipo ObjectId va bene per quello che dobbiamo fare

    private String name;
    private String fileType;

    public Document(ObjectId value) {
        super(value); // value contiene l'id del documento in mongo
        //TODO Auto-generated constructor stub
    }
}
