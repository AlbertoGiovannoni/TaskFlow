package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.service.OrganizationService;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    private OrganizationDAO organizationDAO;

    @PostMapping("/removeUser")
    public ResponseEntity<Map<String, String>> removeUser(
            @RequestBody Map<String, String> requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Ottieni i dati dalla richiesta
        String userId = requestBody.get("userId");
        String organizationId = requestBody.get("organizationId");

        if (userId == null || userId.trim().isEmpty() || organizationId == null || organizationId.trim().isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User ID and Organization ID cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Trova l'utente corrente
        Optional<User> currentUser = userDAO.findByUsername(currentUsername);
        if (!currentUser.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Current user not found");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Trova l'organizzazione
        Optional<Organization> optionalOrganization = organizationDAO.findById(organizationId);
        if (!optionalOrganization.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Organization not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Organization organization = optionalOrganization.get();
        // Verifica se l'utente corrente è OWNER di questa specifica organizzazione
        if (!organization.getOwners().contains(currentUser.get()) && !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Access Denied: You are not an owner of this organization");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        // Trova l'utente da rimuovere
        Optional<User> optionalUser = userDAO.findById(userId);
        if (!optionalUser.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return removeUserFromOrg(optionalUser, organization);
    }

    private ResponseEntity<Map<String, String>> removeUserFromOrg(Optional<User> optionalUser,
            Organization organization) {
        User user = optionalUser.get();

        // Rimuove l'utente dai membri o proprietari dell'organizzazione specifica
        boolean userRemoved = false;
        if (organization.getOwners().contains(user)) {
            organization.removeOwner(user);
            userRemoved = true;
        }
        if (organization.getMembers().contains(user)) {
            organization.removeMember(user);
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
}
