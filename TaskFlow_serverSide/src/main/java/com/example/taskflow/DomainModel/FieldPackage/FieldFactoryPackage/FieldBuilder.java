package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldBuilder {
    FieldType type;
    FieldDefinition fieldDefinition;

    FieldBuilder(FieldType type){
        this.type = type;
    }

    public FieldBuilder addCommonAttributes(FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
        return self();
    }

    public abstract FieldBuilder addParameters(ArrayList<?> values);

    public abstract FieldBuilder addParameter(Object value);

    public FieldBuilder setDate(LocalDateTime date){
        throw new IllegalAccessError(this.self().getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    public FieldBuilder setNotification(Notification notification){
        throw new IllegalAccessError(this.self().getClass().getSimpleName() + " not implement method addSpecificField()");
    }
    
    public FieldBuilder setDocumentName(String name){
        throw new IllegalAccessError(this.self().getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    public FieldBuilder setDocumentFileType(String fileType){
        throw new IllegalAccessError(this.self().getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    public FieldBuilder setDocumentObjectId(ObjectId value){
        throw new IllegalAccessError(this.self().getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    abstract FieldBuilder self();

    public abstract Field build();
}
