package com.example.taskflow.DomainModel.FieldPackage;

import org.bson.types.ObjectId;

public class DocumentInfo {

    private String name;
    private String fileType;
    private ObjectId value;

    public DocumentInfo(String name, String fileType, ObjectId value){
        this.name = name;
        this.fileType = fileType;
        this.value = value;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public ObjectId getValue() {
		return value;
	}
	public void setValue(ObjectId value) {
		this.value = value;
	}
}
