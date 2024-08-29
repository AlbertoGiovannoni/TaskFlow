package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;

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

    public abstract Object getValue();

    public abstract ArrayList<?> getValues();

    public abstract void removeValue(Object value);

    public abstract void removeValues(ArrayList<?> values);

    public abstract void reset();

    public abstract void addValue(Object value);

    public abstract void addValues(ArrayList<?> values);

    public abstract void setValue(Object value);

    public abstract void setValues(ArrayList<?> values);

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public FieldType getType(){
        return this.fieldDefinition.getType();
    }
}
