package com.example.taskflow.DomainModel.FieldPackage;
import java.util.UUID;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Number extends Field{

    private Float value;
    private UUID uuid;

    // costruttore di default
    public Number(){
    }

    public Number(FieldDefinition fieldDefinition, Float value) {
        super(fieldDefinition);

        this.value = value;
        this.uuid = UUID.randomUUID();
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public UUID getUuid() {
        return uuid;
    }    
    
}
