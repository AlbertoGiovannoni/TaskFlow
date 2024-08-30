package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.DateInfo;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class DateBuilder extends FieldBuilder{
    private DateInfo dateInfo;

    DateBuilder(FieldType type) {
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
            if (value instanceof DateInfo){
                this.dateInfo = (DateInfo)value;                   
            }
        }
        return this;
    }

    @Override
    protected DateBuilder self() {
        return this;
    }

    @Override
    public Field build() {
        if (this.dateInfo == null){
            throw new IllegalAccessError("date is null");
        }
        if (this.dateInfo.getNotification() == null){
            return new Date(this.fieldDefinition, dateInfo.getValue());
        }
        else{
            return new Date(this.fieldDefinition, dateInfo);
        }
    }
}