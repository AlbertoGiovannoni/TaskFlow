package com.example.taskflow.DomainModel.FieldPackage;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.annotation.TypeAlias;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

@TypeAlias("SingleSelection")
public class SingleSelection extends Field {

    private String selection;

    // costruttore di default
    public SingleSelection() {
    }

    public SingleSelection(FieldDefinition fieldDefinition, String value) {
        super(fieldDefinition);

        this.selection = value;
    }

    @Override
    public Object getValue() {
        return this.selection;
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            if (value instanceof String) {
                this.selection = (String) value;
            }
        } else {
            throw new IllegalArgumentException("Value is not of type String: \n" + value);
        }
    }

    @Override
    public void setValues(ArrayList<?> values) {
        setValue(values.get(0));
    }

    @Override
    public void removeValue(Object value) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
        + " doesn't implement method " 
        + methodName);
    }

    @Override
    public void removeValues(ArrayList<?> values) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
        + " doesn't implement method " 
        + methodName);
    }

    @Override
    public void addValue(Object value) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
        + " doesn't implement method " 
        + methodName);
    }

    @Override
    public void addValues(ArrayList<?> values) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
        + " doesn't implement method " 
        + methodName);
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public ArrayList<?> getValues() {
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add(this.selection);
        return tmp;
    }

    @Override
    public void reset() {
        this.selection = "";
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        //TODO da fare controllo se la selection è in fielddefinition di singleselection
        this.selection = selection;
    }

    
}
