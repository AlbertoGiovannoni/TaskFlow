package com.unifi.taskflow.daos;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.unifi.taskflow.domainModel.Organization;

public interface OrganizationDAO extends MongoRepository<Organization, String>, CustomOrganizationDAO {
}