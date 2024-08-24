package com.example.taskflow.DomainModel.FieldPackage;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public abstract class Field<T> {
    
    T value;

    @DBRef
    FieldDefinition fieldDefinition;

    public Field(T value, FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
        this.value = value;
    }

    // getter e setter
    
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
