package com.example.taskflow.service.FieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DTOs.Field.DocumentDTO;
import com.example.taskflow.DTOs.Field.DocumentUploadDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Document;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.DocumentBuilder;

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
