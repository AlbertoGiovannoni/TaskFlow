package com.unifi.taskflow.daos;

import java.util.List;

import com.unifi.taskflow.domainModel.Activity;

import java.util.ArrayList;

public interface CustomActivityDAO {
    void removeFieldFromActivity(String fieldId);
    void removeFieldsFromActivities(List<String> fieldIds);
    ArrayList<Activity> getActivitiesByFieldIds(List<String> fieldIds);
}
