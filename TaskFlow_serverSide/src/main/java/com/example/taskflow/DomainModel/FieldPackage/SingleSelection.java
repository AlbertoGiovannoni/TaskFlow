package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class SingleSelection extends Field{

    private ArrayList<String> values;
    private UUID uuid;

    // costruttore di default
    public SingleSelection(){
    }

    public SingleSelection(FieldDefinition fieldDefinition, ArrayList<String> values) {
        super(fieldDefinition);

        this.values = values;
        this.uuid = UUID.randomUUID();
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
                    this.values.add((String)value);
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
        this.reset();
        this.addValues(values);
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
