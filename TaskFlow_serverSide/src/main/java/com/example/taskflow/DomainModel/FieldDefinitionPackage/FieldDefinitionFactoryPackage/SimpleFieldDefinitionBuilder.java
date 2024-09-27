package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import com.example.taskflow.DomainModel.EntityFactory;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;

public class SimpleFieldDefinitionBuilder extends FieldDefinitionBuilder{

    public SimpleFieldDefinitionBuilder(FieldType type) {
        super(type);
    }

    @Override
    public SimpleFieldDefinition build() {
        SimpleFieldDefinition simpleFieldDefinition = EntityFactory.getSimpleFieldDefinition();

        simpleFieldDefinition.setName(this.name);
        simpleFieldDefinition.setType(this.type);

        return simpleFieldDefinition;
    }

    @Override
    public SimpleFieldDefinitionBuilder reset() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + "doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }
}
