package com.unifi.taskflow.businessLogic.services.fieldServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.field.DocumentDTO;
import com.unifi.taskflow.businessLogic.dtos.field.DocumentUploadDTO;
import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Document;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.DocumentBuilder;

@Service
public class DocumentService extends FieldService{

    @Autowired
    private FieldDAO fieldDao;

    public Document download(String documentFieldId){
        Document document = (Document)this.fieldDao.findById(documentFieldId).orElseThrow();

        if (document.getContent() == null){
            throw new IllegalArgumentException("Document is empty");
        }

        return document;
    }

    @Override
    public Field pushNewField(FieldDTO fieldDto) {

        if (!(fieldDto instanceof DocumentUploadDTO)) {
            throw new IllegalArgumentException(
                    "FieldDto of class " + fieldDto.getClass().getSimpleName() + " instead of StringDTO");
        }

        DocumentUploadDTO documentDto = (DocumentUploadDTO) fieldDto;

        FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(documentDto.getFieldDefinitionId())
                .orElse(null);

        if (fieldDefinition == null) {
            throw new IllegalArgumentException("Wrong fieldDefinition id");
        }

        Field field = (new DocumentBuilder(fieldDefinition))
                .addFileName(documentDto.getFileName())
                .addFileType(documentDto.getFileType())
                .addContent(documentDto.getContent())
                .build();

        field = fieldDao.save(field);

        return field;
    }

    @Override
    public Field updateField(FieldDTO fieldDto) {
        if (!(fieldDto instanceof DocumentDTO)){
            throw new IllegalArgumentException("Field is not a DocumentDto");
        }

        DocumentDTO documentDto = (DocumentDTO)fieldDto;

        Document document = (Document)this.fieldDao.findById(documentDto.getId()).orElseThrow();

        if (documentDto.getFileName() != null){
            if (!(documentDto.getFileName().isBlank())){
                document.setFileName(documentDto.getFileName());
            }
        }

        if (documentDto.getFileType() != null){
            if (!(documentDto.getFileType().isBlank())){
                document.setFileType(documentDto.getFileType());
            }
        }

        if (fieldDto instanceof DocumentUploadDTO){
            document.setContent(((DocumentUploadDTO)fieldDto).getContent());
        }

        return this.fieldDao.save(document);
    }
}
