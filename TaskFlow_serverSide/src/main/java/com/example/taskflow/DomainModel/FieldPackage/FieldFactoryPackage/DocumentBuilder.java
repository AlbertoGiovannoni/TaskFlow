package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.util.ArrayList;
import com.example.taskflow.DomainModel.FieldPackage.DateInfo;
import com.example.taskflow.DomainModel.FieldPackage.Document;
import com.example.taskflow.DomainModel.FieldPackage.DocumentInfo;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class DocumentBuilder extends FieldBuilder{
    
    private DocumentInfo documentInfo; 

    DocumentBuilder(FieldType type) {
        super(type);
    }

    @Override
    public FieldBuilder addParameters(ArrayList<?> values){
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + "doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public FieldBuilder addParameter(Object value){
        if (value != null){
            if (value instanceof DateInfo){
                this.documentInfo = (DocumentInfo)value;                   
            }
        }
        return this;
    }

    @Override
    protected DocumentBuilder self() {
        return this;
    }

    @Override
    public Field build() {
        if (this.documentInfo.getName() == null ){
            throw new IllegalAccessError("name is null");
        }
        if (this.documentInfo.getValue() == null ){
            throw new IllegalAccessError("ObjectId is null");
        }
        if (this.documentInfo.getFileType() == null ){
            throw new IllegalAccessError("fileType is null");
        }
        return new Document(documentInfo);
        
    }
}
