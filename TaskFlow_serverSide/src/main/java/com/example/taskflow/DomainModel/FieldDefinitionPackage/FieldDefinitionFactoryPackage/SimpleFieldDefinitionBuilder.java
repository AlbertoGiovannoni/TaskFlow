package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;

public class SimpleFieldDefinitionBuilder extends FieldDefinitionBuilder{

    SimpleFieldDefinitionBuilder(FieldType type) {
        super(type);
    }

    @Override
    public SimpleFieldDefinition build() {
        return new SimpleFieldDefinition(this.name, this.type);
    }

    @Override
    public FieldDefinitionBuilder addParameters(ArrayList<?> values) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + "doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public FieldDefinitionBuilder addParameter(Object value) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + "doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + "doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }
}
