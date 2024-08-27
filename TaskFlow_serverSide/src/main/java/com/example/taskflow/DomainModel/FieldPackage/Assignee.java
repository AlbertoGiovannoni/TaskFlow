package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Assignee extends Field{

    private ArrayList<User> value;
    private UUID uuid;

    public Assignee(FieldDefinition fieldDefinition, ArrayList<User> value) {
        super(fieldDefinition);

        this.value = value;
        this.uuid = UUID.randomUUID();
    }

    public ArrayList<User> getValue() {
        return value;
    }

    public void setValue(ArrayList<User> value) {
        this.value = value;
    }

    public UUID getUuid() {
        return uuid;
    }
}
