package com.example.taskflow.DTOs;

import java.util.ArrayList;

import com.example.taskflow.DTOs.Field.FieldDTO;

import jakarta.validation.constraints.NotNull;

public class ActivityDTO {

    private String id;
    @NotNull
    private String name;
    private ArrayList<FieldDTO> fields;

    // public ActivityDTO(String name, String id, ArrayList<FieldDTO> fields){
    //     this.name = name;
    //     this.fields = fields;
    //     this.id = id;
    // }
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
    public ArrayList<FieldDTO> getFields() {
        return fields;
    }
    public void setFields(ArrayList<FieldDTO> fields) {
        this.fields = fields;
    }
}
