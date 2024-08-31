package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Number;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import java.util.ArrayList;

public class NumberBuilder extends FieldBuilder{
    private Float value;

    NumberBuilder(FieldType type) {
        super(type);
    }

    @Override
    public FieldBuilder addParameters(ArrayList<?> values) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + "doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public NumberBuilder addParameter(Object value){
        if (value != null){
            if (value instanceof User){
                this.value = (Float)value;
            }
        }
        return this;
    }

    @Override
    protected NumberBuilder self() {
        return this;
    }

    @Override
    public Field build() {
        if (this.value != null){            //TODO da testare se funziona questa verifica
            throw new IllegalAccessError("value is null");
        }
        return new Number(this.fieldDefinition, this.value);
    }
}
