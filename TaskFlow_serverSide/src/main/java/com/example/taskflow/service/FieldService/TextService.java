package com.example.taskflow.service.FieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.TextDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.TextBuilder;
import com.example.taskflow.Mappers.FieldMapper;

@Service
public class TextService extends FieldService {

    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    FieldMapper fieldMapper;
    @Autowired
    FieldDAO fieldDao;
    @Autowired
    UserDAO userDAO;

    @Override
    public Field pushNewField(FieldDTO fieldDto) {

        if (!(fieldDto instanceof TextDTO)) {
            throw new IllegalArgumentException(
                    "FieldDto of class " + fieldDto.getClass().getSimpleName() + " instead of StringDTO");
        }

        TextDTO stringDto = (TextDTO) fieldDto;

        FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(stringDto.getFieldDefinitionId())
                .orElse(null);

        if (fieldDefinition == null) {
            throw new IllegalArgumentException("Wrong fieldDefinition id");
        }

        Field field = (new TextBuilder(fieldDefinition))
                .addText(stringDto.getValue())
                .build();

        field = fieldDao.save(field);

        return field;
    }

    @Override
    public Field updateField(FieldDTO fieldDto) {

        Text field = (Text) this.fieldDao.findById(fieldDto.getId()).orElse(null);

        if (field == null){
            throw new IllegalArgumentException("Field text not found");
        }

        TextDTO textDTO = (TextDTO) fieldDto;

        if (textDTO.getValue() != null && textDTO.getValue().length() > 0) {
            field.setValue(textDTO.getValue());
        }
        else{
            throw new IllegalArgumentException("Value per la modifica non può essere vuoto");
        }
        this.fieldDao.save(field);

        return field;
    }
}
