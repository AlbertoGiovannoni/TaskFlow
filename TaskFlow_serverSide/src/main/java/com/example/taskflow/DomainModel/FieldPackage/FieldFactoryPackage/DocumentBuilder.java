package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;
import java.time.LocalDateTime;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class DocumentBuilder extends FieldBuilder{
    private String name;

    DocumentBuilder(FieldType type) {
        super(type);
    }

    @Override
    public DocumentBuilder setDocumentName(String name){
        this.name = name;
        return this.self();
    }

    //TODO implementa altri metodi di set

    @Override
    protected DocumentBuilder self() {
        return this;
    }

    @Override
    public Field build() {
        if (this.name == null){
            throw new IllegalAccessError("name is null");
        }
        return new Document(this.fieldDefinition, name);
        
    }
}
