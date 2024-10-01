package com.example.taskflow.DTOs;

import java.util.ArrayList;

import com.example.taskflow.DTOs.Field.FieldDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class ActivityDTO {

    private String id;
    @NotNull
    private String name;
    @Valid
    @NotNull
    private ArrayList<FieldDTO> fields;

    private String uuid;

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
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
