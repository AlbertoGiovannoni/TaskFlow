package com.example.taskflow.DomainModel.FieldPackage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Date extends Field{
    
    @DBRef
    private Notification notification;
    private LocalDateTime value;
    private UUID uuid;
    
    // costruttore di default
    public Date() {
    }

    public Date(FieldDefinition fieldDefinition, LocalDateTime value) {
        super(fieldDefinition);

        this.value = value;
        this.uuid = UUID.randomUUID();
    }

    public Date(FieldDefinition fieldDefinition, LocalDateTime value, Notification notification) {
        super(fieldDefinition);

        this.notification = notification;
    }

    @Override
    public Notification getNotification() {
        return this.notification;
    }

    @Override
    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void removeNotification(){
        if (this.notification != null){
            this.notification = null;
        }
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void removeValue(Object value) {
        this.value = null;
    }
    
    @Override
    public void reset() {
        this.value = null;
    }

    @Override
    public void setValue(Object value) {
        if (value != null){
            if (value instanceof LocalDateTime){
                this.value = (LocalDateTime)value;
            }
        }
    }

    @Override
    public ArrayList<?> getValues() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void removeValues(ArrayList<?> values) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void addValue(Object value) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void addValues(ArrayList<?> values) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void setValues(ArrayList<?> values) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }
    
}
