package com.example.taskflow.DomainModel;
import java.util.UUID;

public interface UuidInterface {
    default String createUuid(){
        return UUID.randomUUID().toString();
    };

    String getUuid();
    void setUuid(String uuid);
}
