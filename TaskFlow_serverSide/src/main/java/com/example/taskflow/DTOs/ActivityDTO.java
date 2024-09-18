package com.example.taskflow.DTOs;

import java.util.ArrayList;

import jakarta.validation.constraints.NotNull;

public class ActivityDTO {

    private String id;
    @NotNull
    private String name;
    private ArrayList<String> fieldsId; // todo valutare uso di arraylist di fieldDTO per richieste get

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
    public ArrayList<String> getFieldsId() {
        return fieldsId;
    }
    public void setFieldsId(ArrayList<String> fieldsId) {
        this.fieldsId = fieldsId;
    }
}
