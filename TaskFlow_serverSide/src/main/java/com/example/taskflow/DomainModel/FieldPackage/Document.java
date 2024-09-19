package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import java.util.UUID;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.TypeAlias;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

@TypeAlias("Document")
public class Document extends Field{ 
    
    // TODO non so se il tipo ObjectId va bene per quello che dobbiamo fare
    /*
     * FIXME: Forse questo potrebbe essere un pò un wrapper al vero e proprio field di Document
     *        Per esempio considerare l'aggiunta di una classe DocumentInfo e linkare quello
     *        Si potrebbe fare uguale con Date?
     */

    private String name;
    private String fileType;
    private ObjectId value;
    
    // costruttore di default
    public Document(DocumentInfo documentInfo){
        this.name = documentInfo.getName();
        this.fileType = documentInfo.getFileType();
        this.value = documentInfo.getValue();
    }

    public Document(ObjectId value, FieldDefinition fieldDefinition, String name, String fileType) {
        super(fieldDefinition);

        this.value = value;
        this.name = name;
        this.fileType = fileType;
    }
    public Document(){}

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

    @Override
    public ArrayList<?> getValues() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValues'");
    }

    @Override
    public void removeValue(Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeValue'");
    }

    @Override
    public void removeValues(ArrayList<?> values) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeValues'");
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reset'");
    }

    @Override
    public void addValue(Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addValue'");
    }

    @Override
    public void addValues(ArrayList<?> values) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addValues'");
    }

    @Override
    public void setValue(Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setValue'");
    }

    @Override
    public void setValues(ArrayList<?> values) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setValues'");
    }
}
