package com.example.taskflow.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
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

        // Trova l'utente autenticato (esempio di logica personalizzata)
        // Qui recupererai l'utente dal DB o da un altro sistema in base al nome utente.
        Optional<User> currentUser = userDAO.findByUsername(username);
        if (!currentUser.isPresent()) {
            return Set.of(); // Se l'utente non esiste
        }

        // Trova l'organizzazione specifica
        Optional<Organization> optionalOrganization = organizationDAO.findById(organizationId);
        if (!optionalOrganization.isPresent()) {
            return Set.of(); // Se l'organizzazione non esiste
        }

        Organization organization = optionalOrganization.get();

        // Verifica i ruoli dinamici in base all'organizzazione e all'utente
        if (organization.getOwners().contains(currentUser.get())) {
            return Set.of("ROLE_OWNER"); // Se l'utente è un owner
        }

        if(currentUser.get().isAdmin()){
            return Set.of("ROLE_ADMIN");
        }

        // Altrimenti restituisci i ruoli standard o vuoto
        return Set.of("ROLE_USER");
    }
}