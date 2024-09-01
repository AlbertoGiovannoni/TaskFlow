package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    // TODO questo deve essere removeUser dall org. Delete in Admin

    @PostMapping("/removeUser")
    public ResponseEntity<Map<String, String>> removeUser(@RequestBody Map<String, String> requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se l'utente ha il ruolo OWNER o ADMIN
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_OWNER") ||
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Access Denied");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        // Recupera i dati dalla richiesta
        String userId = requestBody.get("userId");
        String organizationId = requestBody.get("organizationId");

        if (userId == null || userId.trim().isEmpty() || organizationId == null || organizationId.trim().isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User ID and Organization ID cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Trova l'utente
        Optional<User> optionalUser = userDAO.findById(userId);

        if (!optionalUser.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        // Trova l'organizzazione
        Optional<Organization> optionalOrganization = organizationDAO.findById(organizationId);

        if (!optionalOrganization.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Organization not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Organization organization = optionalOrganization.get();

        boolean userRemoved = false;

        // Rimuove l'utente dai membri e/o proprietari dell'organizzazione
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