package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import java.util.ArrayList;

public class TextBuilder extends FieldBuilder{
    private String text;

    TextBuilder(FieldType type) {
        super(type);
    }

    @Override
    public FieldBuilder addParameters(ArrayList<?> values){
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + "doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public FieldBuilder addParameter(Object value){
        if (value != null){
            if (value instanceof String){
                this.text = (String)value;
            }
        }
        return this;
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