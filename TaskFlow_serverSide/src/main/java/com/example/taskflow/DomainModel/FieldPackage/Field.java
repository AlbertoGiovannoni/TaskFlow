package com.example.taskflow.DomainModel.FieldPackage;

import org.springframework.data.mongodb.core.mapping.DBRef;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.taskflow.DomainModel.BaseEntity;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

@Document("field") 
public abstract class Field extends BaseEntity{
    @DBRef
    FieldDefinition fieldDefinition;

    // costruttore di default
    public Field() {
        super();
    }

    public Field(String uuid) {
        super(uuid);
    }

    public Field(String uuid, FieldDefinition fieldDefinition) {
        super(uuid);
        this.fieldDefinition = fieldDefinition;
    }

    public FieldType getType(){
        return this.fieldDefinition.getType();
    }

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public void setFieldDefinition(FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
    }
}
