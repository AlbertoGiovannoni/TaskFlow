package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import org.springframework.data.mongodb.core.mapping.Document;
import com.example.taskflow.DomainModel.UuidInterface;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

@Document("field") 
public abstract class Field implements UuidInterface{
    @Id
    String id;
    @DBRef
    FieldDefinition fieldDefinition;
    UUID uuid;

    // costruttore di default
    public Field() {
    }

    public Field(FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
        this.uuid = UUID.randomUUID();
    }

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public FieldType getType(){
        return this.fieldDefinition.getType();
    }

    public String getId(){
        return this.id;
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
    
    @Override
    public boolean equals(Object obj) {
        boolean value = false;

        if (obj != null && obj instanceof Field) {
            if (obj instanceof Field){
                value = (this.uuid.equals(((Field)obj).getUuid()));  
            }
        }

        return value;
    }
}
