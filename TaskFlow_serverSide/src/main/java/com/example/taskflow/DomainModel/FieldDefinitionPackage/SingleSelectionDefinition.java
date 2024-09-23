package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;

public class SingleSelectionDefinition extends FieldDefinition {

    private ArrayList<String> possibleSelections;

    public SingleSelectionDefinition() {
        super();
    }

    public SingleSelectionDefinition(String uuid) {
        super(uuid);
    }

    public SingleSelectionDefinition(String uuid, String name, ArrayList<String> possibleSelections) {
        super(uuid, name, FieldType.SINGLE_SELECTION);

        this.possibleSelections = possibleSelections;
    }

    @Override
    public void addSingleEntry(Object obj){
        if (obj != null){
            if (obj instanceof String){
                if (!this.possibleSelections.contains((String)obj)){
                    this.possibleSelections.add((String)obj);
                }
            }
        }
    }

    @Override
    public void addMultipleEntry(ArrayList<?> objs){
        for (Object obj : objs){
            this.addSingleEntry(obj);
        }
    }

    @Override
    public void removeEntry(Object obj) {
        if (obj != null){
            if (obj instanceof String){
                if (this.possibleSelections != null){
                    if (!this.possibleSelections.isEmpty()){
                        this.possibleSelections.remove(obj);
                    }
                }
            }
        }
    }

    @Override
    public void removeMultipleEntry(ArrayList<?> objs) {
        for (Object obj : objs){
            this.removeEntry(obj);
        }
    }

    @Override
    public void reset() {
        this.possibleSelections.clear();
    }

    @Override
    public ArrayList<?> getAllEntries() {
        return this.possibleSelections;
    }

    @Override
    public Object getSingleEntry() {
        String string = null;

        if (!this.possibleSelections.isEmpty()){
            string = this.possibleSelections.get(0);
        }

        return string;
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
}
