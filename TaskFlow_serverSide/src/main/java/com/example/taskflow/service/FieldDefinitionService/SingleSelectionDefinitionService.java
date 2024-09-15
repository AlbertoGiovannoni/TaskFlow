package com.example.taskflow.service.FieldDefinitionService;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;

public class SingleSelectionDefinitionService extends FieldDefinitionService{
    @Override
    public FieldDefinition addParameters(String idFieldDefinition, ArrayList<String> selections){
        FieldDefinition fieldDefinitionFromDatabase = this.checkFieldDefinitionExistance(idFieldDefinition);

        if (!(fieldDefinitionFromDatabase instanceof SingleSelectionDefinition)){
            throw new IllegalArgumentException("Expected " + SingleSelectionDefinition.class.getSimpleName() + ", find " + fieldDefinitionFromDatabase.getClass().getSimpleName());
        }

        fieldDefinitionFromDatabase.addMultipleEntry(selections);

        return this.fieldDefinitionDao.save(fieldDefinitionFromDatabase);
    }
}
