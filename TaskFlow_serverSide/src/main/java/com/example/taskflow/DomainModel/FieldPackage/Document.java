package com.example.taskflow.DomainModel.FieldPackage;

import org.springframework.data.annotation.Id;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;


public class Document extends Field{

    @Id
    private String id;
    private String fileName;
    private String contentType;
    private byte[] content;

    public Document() {
        super();
    }

    public Document(String uuid) {
        super(uuid);
    }

    public Document(String uuid, FieldDefinition fieldDefinition, String name) {
        super(uuid, fieldDefinition);
        this.fileName = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
