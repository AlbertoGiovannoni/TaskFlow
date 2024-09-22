package com.example.taskflow.DAOs;

import java.util.List;

public interface ActivityCustomInterfaceDAO {
    void removeFieldFromActivity(String fieldId);
    void removeFieldsFromActivities(List<String> fieldIds);
}
