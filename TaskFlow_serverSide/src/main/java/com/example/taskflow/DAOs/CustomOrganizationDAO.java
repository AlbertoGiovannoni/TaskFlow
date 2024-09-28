package com.example.taskflow.DAOs;

import java.util.ArrayList;
import com.example.taskflow.DomainModel.Organization;

public interface CustomOrganizationDAO {
    ArrayList<Organization> getOrganizationByUser(String userId);
    Organization getOrganizationByProject(String projectId);
}
