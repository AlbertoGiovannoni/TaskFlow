package com.example.taskflow.DomainModel.FieldPackage;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public abstract class Field {
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
}
