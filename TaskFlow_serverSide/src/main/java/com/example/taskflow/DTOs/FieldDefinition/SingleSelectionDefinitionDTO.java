package com.example.taskflow.DTOs.FieldDefinition;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SingleSelectionDefinitionDTO extends FieldDefinitionDTO{
    ArrayList<String> selections;

    // public SingleSelectionDefinitionDTO(){
    //     this.type = FieldType.SINGLE_SELECTION;
    // }

    // @JsonCreator
    // public SingleSelectionDefinitionDTO(@JsonProperty("type") FieldType type) {
    //     this.type = type;
    // }

    // @JsonSetter("type")
    // public void setType(String type) {
    //     this.type = type != null ? FieldType.valueOf(type) : null;
    // }

    public ArrayList<String> getSelections() {
        return selections;
    }

    public void setSelections(ArrayList<String> selections) {
        this.selections = selections;
    }
}
