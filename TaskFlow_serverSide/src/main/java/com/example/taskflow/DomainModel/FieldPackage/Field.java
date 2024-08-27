package com.example.taskflow.DomainModel.FieldPackage;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.example.taskflow.DomainModel.UuidInterface;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class Field implements UuidInterface{
    @DBRef
    FieldDefinition fieldDefinition;

    // costruttore di default
    public Field() {
    }

    public Field(FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
    }

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public FieldType getType(){
        return this.fieldDefinition.getType();
    }
}
