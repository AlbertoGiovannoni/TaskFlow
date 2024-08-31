package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class SingleSelection extends Field{

    private ArrayList<String> values;

    // costruttore di default
    public SingleSelection(){
    }

    public SingleSelection(FieldDefinition fieldDefinition, ArrayList<String> values) {
        super(fieldDefinition);

        this.values = values;
    }

    @Override
    public ArrayList<?> getValues() {
        return this.values;
    }

    @Override
    public void removeValue(Object value) {
        if (value != null){
            if (value instanceof String){
                if (this.values.contains((String)value)){
                    this.values.remove((String)value);
                }
            }
        }
    }

    @Override
    public void removeValues(ArrayList<?> values) {
        for (Object value : values){
            this.removeValue(value);
        }
    }

    @Override
    public void reset() {
        this.values.clear();
    }

    @Override
    public void addValue(Object value) {
        if (value != null){
            if (value instanceof String){
                if (!this.values.contains((String)value)){
                    if (this.fieldDefinition.validateValue((String)value)){
                        this.values.add((String)value);
                    }
                    else{
                        throw new IllegalArgumentException((String)value + 
                                                " cannot be added if corrispondig SingleSelection don't have it as a selection: \n" + 
                                                this.fieldDefinition.getAllEntries());
                    }
                }
            }
        }
    }

    @Override
    public void addValues(ArrayList<?> values) {
        for (Object value : values){
            this.addValue(value);
        }
    }

    @Override
    public void setValue(Object value) {
        if (value != null){
            if (value instanceof String){
                this.reset();
                this.addValue(value);
            }
        }
    }

    @Override
    public void setValues(ArrayList<?> values) {
        boolean valuesAreUsers = this.checkArrayListValidity();
        
        if (valuesAreUsers){
            this.reset();
            for (Object value : values){
                if (value != null){
                    if (value instanceof String){
                        if (!this.values.contains((String)value)){
                            this.values.add((String)value);
                        }
                    }
                }
            }
        }
    }

    private boolean checkArrayListValidity(){
        boolean valuesAreStrings = false;
        
        if (values != null){
            for (Object value : values){
                if (value != null){
                    if (value instanceof String){
                        valuesAreStrings = true;
                    }
                }
            }
        }

        return valuesAreStrings;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }
}
