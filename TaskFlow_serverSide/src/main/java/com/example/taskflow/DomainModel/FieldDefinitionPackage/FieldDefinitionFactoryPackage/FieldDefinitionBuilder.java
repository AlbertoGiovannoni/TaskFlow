package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldDefinitionBuilder {
    String name;
    FieldType type;
    UUID uuid;

    FieldDefinitionBuilder(FieldType type){
        this.type = type;
        this.uuid = UUID.randomUUID();
    }

    public FieldDefinitionBuilder addCommonAttributes(String name) {
        this.name = name;
        return self();
    }

    public FieldDefinitionBuilder setUsers(ArrayList<User> values){
        throw new IllegalAccessError(this.self().getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    public FieldDefinitionBuilder setString(ArrayList<String> values){
        throw new IllegalAccessError(this.self().getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    //TODO: fare altri metodi per aggiunta di array e per set di un solo valore;

    abstract FieldDefinitionBuilder self();

    public abstract FieldDefinition build();
}

