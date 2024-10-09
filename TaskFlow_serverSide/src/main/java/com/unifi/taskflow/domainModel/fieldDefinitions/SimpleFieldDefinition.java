package com.unifi.taskflow.domainModel.fieldDefinitions;

import java.lang.UnsupportedOperationException;

public class SimpleFieldDefinition extends FieldDefinition {
    public SimpleFieldDefinition() {
        super();
    }

    public SimpleFieldDefinition(String uuid) {
        super(uuid);
    }

    public SimpleFieldDefinition(String uuid, String name, FieldType type) {
        super(uuid, name, type);
    }

    @Override
    public void reset() {
        this.name = "";
    }

    @Override
    public boolean validateValue(Object obj) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }
}