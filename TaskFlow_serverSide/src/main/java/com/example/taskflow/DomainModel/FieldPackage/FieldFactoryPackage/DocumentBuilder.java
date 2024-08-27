package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import org.bson.types.ObjectId;

import com.example.taskflow.DomainModel.FieldPackage.Document;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class DocumentBuilder extends FieldBuilder{
    private String name;
    private String fileType;
    private ObjectId value;

    DocumentBuilder(FieldType type) {
        super(type);
    }

    @Override
    public DocumentBuilder setDocumentName(String name){
        this.name = name;
        return this.self();
    }

    @Override
    public DocumentBuilder setDocumentFileType(String fileType){
        this.fileType = fileType;
        return this.self();
    }

    @Override
    public DocumentBuilder setDocumentObjectId(ObjectId value){
        this.value = value;
        return this.self();
    }

    @Override
    protected DocumentBuilder self() {
        return this;
    }

    @Override
    public Field build() {
        if (this.name == null ){
            throw new IllegalAccessError("name is null");
        }
        if (this.value == null ){
            throw new IllegalAccessError("ObjectId is null");
        }
        if (this.fileType == null ){
            throw new IllegalAccessError("fileType is null");
        }
        return new Document( this.value, this.fieldDefinition, this.name, this.fileType);
        
    }
}
