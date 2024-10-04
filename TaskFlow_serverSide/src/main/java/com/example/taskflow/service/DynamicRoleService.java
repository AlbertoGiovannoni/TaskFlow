package com.example.taskflow.service;

import org.springframework.stereotype.Service;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import java.util.Set;

@Service
public class DynamicRoleService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    public Set<String> getRolesBasedOnContext(String organizationId, Authentication authentication) {
        // Recupera il nome utente corrente
        String username = authentication.getName();

        // Trova l'utente autenticato
        Optional<User> currentUser = userDAO.findByUsername(username);
        if (!currentUser.isPresent()) {
            return Set.of();
        }

        // Trova l'organizzazione specifica
        Optional<Organization> optionalOrganization = organizationDAO.findById(organizationId);
        if (!optionalOrganization.isPresent()) {
            return Set.of();
        }

        Organization organization = optionalOrganization.get();

        // Verifica i ruoli in base all'organizzazione e all'utente
        if (organization.getOwners().contains(currentUser.get())) {
            return Set.of("ROLE_OWNER");
        }
        return Set.of("ROLE_USER");
    }
}