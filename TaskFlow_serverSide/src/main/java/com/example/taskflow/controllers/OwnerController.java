package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.service.OrganizationService;

/*
    FIXME: fixare tutti i metodi ed utilizzare i DTO! 
    [probabilmente va cancellato tutto e rifatto da capo :(]
*/

@RestController
@RequestMapping("/api/user")
public class OwnerController {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ProjectDAO projectDAO;

    @Autowired
    private OrganizationDAO organizationDAO;

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Access Denied: You are not authorized to perform this action.");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') or "
            +
            "@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_ADMIN')")
    @DeleteMapping("/{userId}/myOrganization/{organizationId}/users/{targetId}")
    public ResponseEntity<Map<String, String>> removeUser(@PathVariable String targetId, @RequestBody Map<String, String> requestBody) {
        
        Map<String, String> response = new HashMap<>();
    
        // Ottieni i dati dalla richiesta
        String organizationId = requestBody.get("organizationId");

        if (targetId == null || targetId.trim().isEmpty() || organizationId == null || organizationId.trim().isEmpty()) {
            response.put("message", "User ID and Organization ID cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Trova l'organizzazione
        Optional<Organization> optionalOrganization = organizationDAO.findById(organizationId);
        if (!optionalOrganization.isPresent()) {
            response.put("message", "Organization not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Organization organization = optionalOrganization.get();

        // Trova l'utente da rimuovere
        User target = userDAO.findById(targetId).orElse(null);

        if (target == null){
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
            
        return removeUserFromOrg(target, organization);
    }

    private ResponseEntity<Map<String, String>> removeUserFromOrg(User target,
            Organization organization) {
        

        // Rimuove l'utente dai membri o proprietari dell'organizzazione specifica
        boolean userRemoved = false;
        if (organization.getOwners().contains(target)) {
            organization.removeOwner(target);
            userRemoved = true;
        }
        if (organization.getMembers().contains(target)) {
            organization.removeMember(target);
            userRemoved = true;
        }

        // Salva l'organizzazione aggiornata
        if (userRemoved) {
            organizationDAO.save(organization);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User removed from the organization");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found in the specified organization");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') or "
    +
    "@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_ADMIN')")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects")
    public ResponseEntity<Map<String, String>> createProject(@RequestBody Map<String, Object> requestBody) {

        // TODO autenticazione owner

        Map<String, String> response = new HashMap<>();
        String name = (String) requestBody.get("projectName");

        // Controllo e conversione dell'input
        if (name == null || name.trim().isEmpty()) {
            response.put("message", "projectName cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Project project = new Project(name);
        projectDAO.save(project);

        response.put("message", "Progetto creato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') or "
    +
    "@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_ADMIN')")
    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}")
    public ResponseEntity<Map<String, String>> renameProject(@PathVariable String projectId, @RequestBody Map<String, Object> requestBody) {

        // TODO autenticazione owner

        Map<String, String> response = new HashMap<>();

        String name = (String) requestBody.get("newName");

        // Controllo e conversione dell'input
        if (name == null || name.trim().isEmpty()) {
            response.put("message", "projectName cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (projectId == null) {
            response.put("message", "projectId cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Project project = projectDAO.findById(projectId).orElse(null);
        if (project == null) {
            response.put("message", "wrong projectId");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        project.setName(name);
        projectDAO.save(project);

        response.put("message", "Progetto rinominato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
