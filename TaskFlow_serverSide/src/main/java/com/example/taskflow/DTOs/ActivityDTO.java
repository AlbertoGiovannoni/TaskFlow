package com.example.taskflow.DTOs;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldPackage.Field;

import jakarta.validation.constraints.NotNull;

public class ActivityDTO {

    private String id;
    @NotNull
    private String name;
    private ArrayList<Field> fields;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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

}
