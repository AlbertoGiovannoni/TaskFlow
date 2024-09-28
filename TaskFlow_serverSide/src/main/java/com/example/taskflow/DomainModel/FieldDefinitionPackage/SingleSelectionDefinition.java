package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;

import com.example.taskflow.DomainModel.User;

public class SingleSelectionDefinition extends FieldDefinition {

    private ArrayList<String> possibleSelections;

    public SingleSelectionDefinition() {
        super();
        this.type = FieldType.SINGLE_SELECTION;
    }

    public SingleSelectionDefinition(String uuid) {
        super(uuid);
        this.type = FieldType.SINGLE_SELECTION;
    }

    public SingleSelectionDefinition(String uuid, String name, ArrayList<String> possibleSelections) {
        super(uuid, name, FieldType.SINGLE_SELECTION);

        this.possibleSelections = possibleSelections;
    }

    @Override
    public void reset() {
        if (this.possibleSelections != null){
            this.possibleSelections.clear();
        }
    }

    @Override
    public boolean validateValue(Object obj) {
        boolean validation = false;
        
        if (obj != null){
            if (obj instanceof String){
                validation = this.possibleSelections.contains((String)obj);
            }
        }

        return validation;
    }

    public ArrayList<String> getPossibleSelections() {
        return possibleSelections;
    }

    public void setPossibleSelections(ArrayList<String> possibleSelections) {
        this.possibleSelections = possibleSelections;
    }

    public void addSelection(String s) {
        this.possibleSelections.add(s);
    }

    public void addMultipleSelection(ArrayList<String> selections) {
        for(String s : selections){
            this.addSelection(s);
        }
    }

    public void removeSelection(String selection) {
        if (selection != null) {
            if (this.possibleSelections != null) {
                if (!this.possibleSelections.isEmpty()) {
                    this.possibleSelections.add(selection);
                }
            }

        }
    }

    public void removeMultipleSelection(ArrayList<String> selections) {
        for (String selection : selections) {
            this.removeSelection(selection);
        }
    }
}
