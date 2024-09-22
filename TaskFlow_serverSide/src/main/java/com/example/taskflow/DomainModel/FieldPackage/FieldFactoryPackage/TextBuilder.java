package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class TextBuilder extends FieldBuilder {
    private String text;

    public TextBuilder(FieldDefinition fieldDefinition) {
        super(fieldDefinition);
    }

    public FieldBuilder addText(String value) {
        if (value != null) {
            this.text = value;
        } else {
            throw new IllegalArgumentException("text is null:" + value);
        }
        return this;
    }

    @Override
    public Field build() {
        if (this.text == null) {
            throw new IllegalAccessError("text is null");
        }
        return new Text(this.fieldDefinition, text);
    }
}