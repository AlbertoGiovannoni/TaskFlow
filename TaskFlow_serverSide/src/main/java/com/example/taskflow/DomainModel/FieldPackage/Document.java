package com.example.taskflow.DomainModel.FieldPackage;


import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;


public class Document extends Field{

    private String fileName;
    private String fileType;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
