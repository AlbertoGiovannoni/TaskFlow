package com.example.taskflow.DAOs;

import java.util.List;
import java.util.ArrayList;

import com.example.taskflow.DomainModel.Activity;

public interface CustomActivityDAO {
    void removeFieldFromActivity(String fieldId);
    void removeFieldsFromActivities(List<String> fieldIds);
    ArrayList<Activity> getActivitiesByFieldIds(List<String> fieldIds);
}
