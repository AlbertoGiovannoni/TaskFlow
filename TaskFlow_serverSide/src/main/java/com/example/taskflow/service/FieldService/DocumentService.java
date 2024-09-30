package com.example.taskflow.service.FieldService;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.FieldPackage.Document;
import com.example.taskflow.DomainModel.FieldPackage.Field;

@Service
public class DocumentService extends FieldService{

    @Autowired
    private FieldDAO fieldDao;

    public Document savePdf(MultipartFile file) throws IOException{
        Document pdfDocument = new Document();
        pdfDocument.setFileName(file.getOriginalFilename());
        pdfDocument.setContentType(file.getContentType());
        pdfDocument.setContent(file.getBytes());

        return fieldDao.save(pdfDocument);
    }
    public Document getDocumentById(String id) {
        Document document = (Document)this.fieldDao.findById(id).orElseThrow();
        return document;
    }
    @Override
    public Field pushNewField(FieldDTO fieldDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pushNewField'");
    }
    @Override
    public Field updateField(FieldDTO fieldDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateField'");
    }
}
