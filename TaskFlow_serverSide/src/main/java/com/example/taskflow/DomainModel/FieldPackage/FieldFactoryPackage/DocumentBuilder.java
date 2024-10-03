package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import com.example.taskflow.DomainModel.EntityFactory;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Document;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Text;

public class DocumentBuilder extends FieldBuilder {
    private String fileName;
    private String fileType;
    private byte[] content;

    public DocumentBuilder(FieldDefinition fieldDefinition) {
        super(fieldDefinition);
    }

    public DocumentBuilder addFileName(String value) {
        if (value != null) {
            this.fileName = value;
        } else {
            throw new IllegalArgumentException("file name is null:" + value);
        }
        return this;
    }

    public DocumentBuilder addFileType(String value) {
        if (value != null) {
            this.fileType = value;
        } else {
            throw new IllegalArgumentException("file type is null:" + value);
        }
        return this;
    }

    public DocumentBuilder addContent(byte[] content){
        if (content != null){
            this.content = content;
        }
        return this;
    }

    @Override
    public Field build() {
        if (this.fileName == null) {
            throw new IllegalAccessError("file name is null");
        }

        if (this.fileType == null) {
            throw new IllegalAccessError("file type is null");
        }
        Document document = EntityFactory.getDocument();

        document.setFieldDefinition(this.fieldDefinition);
        document.setFileName(this.fileName);
        document.setFileType(this.fileType);

        return document;
    }

}
