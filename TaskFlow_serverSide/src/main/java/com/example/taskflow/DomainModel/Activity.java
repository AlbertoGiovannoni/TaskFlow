package com.example.taskflow.DomainModel;
import java.util.ArrayList;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.taskflow.DomainModel.FieldPackage.Field;

@Document("activity")
public class Activity extends BaseEntity{
    private String name;
    @DBRef
    private ArrayList<Field> fields;

    // costruttore di default
    public Activity() {
        super();
    }

    public Activity(String uuid) {
        super(uuid);
        this.fields = new ArrayList<Field>();
    }

    public Activity(String uuid, String name) {
        super(uuid);
        this.name = name;
        this.fields = new ArrayList<Field>();
    }

    public Activity(String uuid, String name, ArrayList<Field> fields) {
        super(uuid);
        this.name = name;
        this.fields = fields;
    }

    public void addField(Field field) {
        if (this.fields == null){
            this.fields = new ArrayList<>();
        }
        fields.add(field);
    }

    public void removeField(Field field) {
        if (this.fields != null){
            fields.remove(field);
        }
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

    public void addFields(ArrayList<Field> fields){
        if(this.fields == null){
            this.fields = new ArrayList<>();
        }
        this.fields.addAll(fields);
    }
}
