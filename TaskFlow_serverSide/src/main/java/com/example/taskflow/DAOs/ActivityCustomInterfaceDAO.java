package com.example.taskflow.DAOs;

import java.util.List;

public interface ActivityCustomInterfaceDAO {
    void deleteFieldFromActivity(String fieldId);
    void deleteFieldsFromActivities(List<String> fieldIds);
}
