/* */
package com.example.taskflow.controllers;

import org.apache.el.stream.Optional;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DomainModel.FieldPackage.Document;
import com.example.taskflow.service.FieldService.DocumentService;
import com.example.taskflow.service.FieldService.FieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // Endpoint per caricare un PDF
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            Document pdfDocument = this.documentService.savePdf(file);
            return new ResponseEntity<>("File uploaded successfully: " + pdfDocument.getId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint per restituire un PDF al frontend
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String id) {
        Document document = this.documentService.getDocumentById(id);

        if (document != null) {

            // Imposta gli header della risposta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("inline", document.getFileName()); // Inline visualizza nel browser

            // Restituisci il file PDF come byte[]
            return new ResponseEntity<>(document.getContent(), headers, HttpStatus.OK);
        } else {
            // Restituisci 404 se non trovato
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
