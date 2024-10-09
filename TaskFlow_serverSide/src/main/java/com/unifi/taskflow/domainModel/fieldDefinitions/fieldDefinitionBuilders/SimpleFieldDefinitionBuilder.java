package com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders;

import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;
import com.unifi.taskflow.domainModel.fieldDefinitions.SimpleFieldDefinition;

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
