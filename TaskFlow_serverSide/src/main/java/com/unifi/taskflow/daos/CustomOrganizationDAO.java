package com.unifi.taskflow.daos;

import java.util.ArrayList;

import com.unifi.taskflow.domainModel.Organization;

public interface CustomOrganizationDAO {
    ArrayList<Organization> getOrganizationByUser(String userId);
    Organization getOrganizationByProject(String projectId);
}
