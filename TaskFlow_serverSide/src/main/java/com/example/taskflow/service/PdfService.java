package com.example.taskflow.service;

import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Service;
import org.bson.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.util.Date;

@Service
public class PdfService {

    private final GridFSBucket gridFSBucket;

    @Autowired
    public PdfService(MongoDatabaseFactory mongoDatabaseFactory) {
        this.gridFSBucket = GridFSBuckets.create(mongoDatabaseFactory.getMongoDatabase());
    }

    public ObjectId savePdf(String filePath, String fileName) throws IOException {
        try (InputStream streamToUploadFrom = new FileInputStream(filePath)) {
            GridFSUploadOptions options = new GridFSUploadOptions()
                    .metadata(new Document("type", "pdf").append("upload_date", new Date()));

            ObjectId fileId = gridFSBucket.uploadFromStream(fileName, streamToUploadFrom, options);
            return fileId;
        }
    }

    public void getPdf(ObjectId fileId, String outputPath) throws IOException {
        try (OutputStream streamToDownloadTo = new FileOutputStream(outputPath)) {
            gridFSBucket.downloadToStream(fileId, streamToDownloadTo);
        }
    }
}