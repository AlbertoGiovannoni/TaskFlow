package com.example.taskflow.service.FieldDefinitionServices;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;


@Service
public class FieldDefinitionService {
    @Autowired
    FieldDefinitionDAO fieldDefinitionDao;

    @Autowired
    FieldDAO fieldDao;

    @Autowired
    UserDAO userDao;

    //FIXME: questa operazione probabilmente va fatta direttamente nel controller
    public void delete(String id){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(id).orElseThrow();

        this.fieldDao.deleteFieldByFieldDefinition(fieldDefinition);

        this.fieldDefinitionDao.delete(fieldDefinition);
    }
    
    /*
     * FIXME: Capire se fra controller e services si passano solo DTOs e quindi 
     *        il mapping è fatto sempre nei services oppure se il mapping
     *        è fatto nei controllers 
     */

    // seguendo la filosofia sopra dovrei avere metodi di questo tipo
    public FieldDefinitionDTO createFieldDefinition(FieldDefinitionDTO fieldDefinitionDto){
        // mapping da dto a fielddefininition di seguito creo un field 
        // a caso giusto per avere coerenza
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(FieldType.NUMBER)
                                                                .setName("random")
                                                                .build();
        //salvo il field definition che ho creato
        FieldDefinition fieldDefinitionFromDatabase = this.fieldDefinitionDao.save(fieldDefinition);
        // rimappo il field definition dal database ad un dto e lo ritorno 
        return new FieldDefinitionDTO();
    }

    public FieldDefinition createFieldDefinition(FieldDefinition fieldDefinition){
        return this.fieldDefinitionDao.save(fieldDefinition);
    }

    public FieldDefinition createFieldDefinition(FieldType type, String name){
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(type)
                                            .setName(name)
                                            .build();

        this.fieldDefinitionDao.save(fieldDefinition);
        return fieldDefinition;
    }

    public FieldDefinition createFieldDefinition(FieldType type, String name, ArrayList<?> parameters){
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(type)
                                            .setName(name)
                                            .addParameters(parameters)
                                            .build();
        this.fieldDefinitionDao.save(fieldDefinition);
        return fieldDefinition;
    }

    public FieldDefinition modifyName(String id, String newName){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(id).orElseThrow();

        fieldDefinition.setName(newName);

        return this.fieldDefinitionDao.save(fieldDefinition);
    }

    public FieldDefinition addParameters(String idFieldDefinition, ArrayList<String> userIds){
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    FieldDefinition checkFieldDefinitionExistance(String fieldDefinitionId){
        return this.fieldDefinitionDao.findById(fieldDefinitionId).orElseThrow();
    }
}
