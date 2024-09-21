package com.example.taskflow.DomainModel;
import java.util.ArrayList;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.taskflow.DomainModel.FieldPackage.Field;

@Document("activity")
public class Activity implements UuidInterface{

    @Id
    private String id;
    private String name;
    @DBRef
    private ArrayList<Field> fields;
    private String uuid;

    // costruttore di default
    public Activity() {
    }

    public Activity(String name) {
        this.name = name;
        this.fields = new ArrayList<Field>();
        this.uuid = this.createUuid();
    }

    public Activity(String name, ArrayList<Field> fields) {
        this.name = name;
        this.fields = fields;
        this.uuid = this.createUuid();
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public void removeField(Field field) {
        fields.remove(field);
    }

    // getter e setter

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public boolean equals(Object obj) {
        boolean value = false;

        if (obj != null && obj instanceof Activity){
            if (obj instanceof Activity){
                value = (this.uuid.equals(((Activity)obj).getUuid()));  
            }
        }

        return value;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }
}
