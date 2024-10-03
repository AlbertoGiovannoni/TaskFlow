package com.example.taskflow.service.FieldService;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DTOs.Field.DocumentDTO;
import com.example.taskflow.DTOs.Field.DocumentUploadDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.TextDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Document;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.DocumentBuilder;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.TextBuilder;
import com.example.taskflow.Mappers.FieldMapper;

@Service
public class DocumentService extends FieldService{

    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private FieldMapper fieldMapper;

    public Document pushDocument(DocumentDTO documentDto, MultipartFile file) throws IOException{
        Document document = this.fieldMapper.toEntity(documentDto);
        document.setContent(file.getBytes());

        return fieldDao.save(document);
    }

    public Document getDocumentById(String id) {
        Document document = (Document)this.fieldDao.findById(id).orElseThrow();
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateField'");
    }
}
