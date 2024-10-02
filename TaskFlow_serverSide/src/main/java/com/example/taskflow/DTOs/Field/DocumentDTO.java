package com.example.taskflow.DTOs.Field;

public class DocumentDTO extends FieldDTO {

    private String fileName;
    private String fileType;

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
