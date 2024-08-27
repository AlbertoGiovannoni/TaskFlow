package com.example.taskflow;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.service.PdfService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@Import(PdfService.class)
@ActiveProfiles("test")
public class PdfServiceTest {

    @Autowired
    private PdfService pdfService;

    @Test
    public void testSavePdf() throws IOException {
        // Path al file PDF che vuoi caricare (es. /Users/nomeutente/Documents/file.pdf)
        String filePath = "/Users/gilbe/Desktop/tn.pdf";
        String outputPath = "/Users/gilbe/Downloads/output.pdf";
        String fileName = "tn.pdf";

        // Salva il PDF in MongoDB
        ObjectId fileId = pdfService.savePdf(filePath, fileName);

        pdfService.getPdf(fileId, outputPath);

        // Verifica che il fileId non sia null (quindi che il salvataggio sia andato a buon fine)
        assertNotNull(fileId);

        // Puoi anche aggiungere ulteriori test per recuperare il file e verificarne l'integrità
    }
}