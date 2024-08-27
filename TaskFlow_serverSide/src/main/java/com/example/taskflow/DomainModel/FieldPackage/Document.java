package com.example.taskflow.DomainModel.FieldPackage;
import java.util.UUID;
import org.bson.types.ObjectId;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
public class Document extends Field{ // TODO non so se il tipo ObjectId va bene per quello che dobbiamo fare

    private String name;
    private String fileType;
    private ObjectId value;
    private UUID uuid;
    
    // costruttore di default
    public Document(){
    }

    public Document(ObjectId value, FieldDefinition fieldDefinition, String name, String fileType) {
        super(fieldDefinition);

        this.value = value;
        this.name = name;
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public ObjectId getValue() {
        return value;
    }

    public void setValue(ObjectId value) {
        this.value = value;
    }

    public UUID getUuid() {
        return uuid;
    }
}
