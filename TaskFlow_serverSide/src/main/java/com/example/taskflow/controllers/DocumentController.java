/* */
package com.example.taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.NoSuchElementException;

import com.example.taskflow.DomainModel.FieldPackage.Document;
import com.example.taskflow.service.FieldService.DocumentService;

@RestController
@RequestMapping("/api/user")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/{fieldId}/download")
    public ResponseEntity<?> downloadPdf(@PathVariable String fieldId) {

        try{

            Document document = this.documentService.download(fieldId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("inline", document.getFileName() + "." + document.getFileType()); // Inline visualizza nel browser

            return new ResponseEntity<>(document.getContent(), headers, HttpStatus.OK);

        } catch (NoSuchElementException exception) { 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
