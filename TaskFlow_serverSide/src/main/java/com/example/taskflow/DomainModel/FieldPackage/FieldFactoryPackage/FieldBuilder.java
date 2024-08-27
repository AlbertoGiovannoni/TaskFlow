package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.User;
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

    public FieldBuilder setUsers(ArrayList<User> values){
        throw new IllegalAccessError(this.self().getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    public FieldBuilder setSelection(ArrayList<String> values){
        throw new IllegalAccessError(this.self().getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    abstract FieldBuilder self();

    public abstract Field build();
}
