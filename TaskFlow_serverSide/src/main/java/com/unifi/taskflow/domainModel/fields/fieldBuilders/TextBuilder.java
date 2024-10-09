package com.unifi.taskflow.domainModel.fields.fieldBuilders;

import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.Text;

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

        Text text = EntityFactory.getText();

        text.setFieldDefinition(this.fieldDefinition);
        text.setValue(this.text);

        return text;
    }
}