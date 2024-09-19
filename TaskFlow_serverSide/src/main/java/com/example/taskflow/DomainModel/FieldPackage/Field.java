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

    //TODO: rimuovere questo da qua e metterlo nelle classi figlie specifico così che si possa fare controlli ulteriori
    @DBRef
    FieldDefinition fieldDefinition;
    String uuid;

    // costruttore di default
    public Field() {
    }

    public Field(FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
        this.uuid = this.createUuid();
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

    public void setId(String id) {
        this.id = id;
    }

    public void setFieldDefinition(FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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
