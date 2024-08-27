package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Number;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class NumberBuilder extends FieldBuilder{
    private Float value;

    NumberBuilder(FieldType type) {
        super(type);
    }

    @Override
    public NumberBuilder setNumber(Float value){
        this.value = value;
        return this.self();
    }

    @Override
    protected NumberBuilder self() {
        return this;
    }

    @Override
    public Field build() {
        if (this.value == 0.0f){            //TODO da testare se funziona questa verifica
            throw new IllegalAccessError("value is null");
        }
        return new Number(this.fieldDefinition, this.value);
        
    }
}
