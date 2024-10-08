package com.unifi.taskflow.domainModel;

import org.springframework.data.annotation.Id;

public abstract class BaseEntity {
    @Id
    private String id;

    private String uuid;

    public BaseEntity(){}

    public BaseEntity(String uuid){
        if (uuid == null){
            throw new IllegalArgumentException("uuid cannot be null");
        }
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (!(obj instanceof BaseEntity)){
            return false;
        }
        return uuid.equals(((BaseEntity)obj).getUuid());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
