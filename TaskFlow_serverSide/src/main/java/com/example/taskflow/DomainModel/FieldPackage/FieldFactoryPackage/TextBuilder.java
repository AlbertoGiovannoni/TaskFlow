package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;
import java.time.LocalDateTime;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class TextBuilder extends FieldBuilder{
    private String text;

    TextBuilder(FieldType type) {
        super(type);
    }

    @Override
    public TextBuilder setText(String text){
        this.text = text;
        return this.self();
    }

    @Override
    protected TextBuilder self() {
        return this;
    }

    @Override
    public Field build() {
        if (this.text == null){
            throw new IllegalAccessError("value is null");
        }
        return new Text(this.fieldDefinition, text);
        
    }
}