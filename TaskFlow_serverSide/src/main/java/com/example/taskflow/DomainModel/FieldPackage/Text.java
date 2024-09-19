package com.example.taskflow.DomainModel.FieldPackage;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.annotation.TypeAlias;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

//@TypeAlias("Text")
public class Text extends Field{

    private String text;

    // costruttore di default
    public Text(){
    }

    public Text(FieldDefinition fieldDefinition, String value) {
        super(fieldDefinition);

        this.text = value;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Object getValue() {
        return this.text;
    }

    @Override
    public void reset() {
        this.text = "";
    }

    @Override
    public void setValue(Object value) {
        if (value != null){
            if (value instanceof String){
                this.text = (String)value;
            }
        }
    }

    @Override
    public ArrayList<?> getValues() {
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add(this.text);
        return tmp;
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
    public void setValues(ArrayList<?> values) {
        setValue(values.get(0));
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
