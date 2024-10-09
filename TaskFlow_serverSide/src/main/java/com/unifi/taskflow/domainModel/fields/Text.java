package com.unifi.taskflow.domainModel.fields;

import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;

public class Text extends Field{

    private String value;

    // costruttore di default
    public Text(){
        super();
    }

    public Text(String uuid){
        super(uuid);
    }

    public Text(String uuid, FieldDefinition fieldDefinition, String value) {
        super(uuid, fieldDefinition);

        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String text) {
        this.value = text;
    }
}
