package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;

public class SingleSelectionDefinition extends FieldDefinition {

    private ArrayList<String> possibleSelections;

    public SingleSelectionDefinition() {}

    public SingleSelectionDefinition(String nome, FieldType type, ArrayList<String> possibleSelections) {
        super(nome, type);
        // FIXME: assicurarsi che sia un set
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
}
