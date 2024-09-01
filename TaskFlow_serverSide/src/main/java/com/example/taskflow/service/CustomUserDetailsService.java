package com.example.taskflow.service;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDao;

    @Autowired
    private OrganizationDAO organizationDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    
        // Ottieni le autorità (ruoli) per l'utente
        List<GrantedAuthority> authorities = getAuthoritiesForUser(user);
    
        // Crea e ritorna l'oggetto UserDetails con i ruoli appropriati
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getUserInfo().getPassword(), authorities);
    }

    private List<GrantedAuthority> getAuthoritiesForUser(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Cerca tutte le organizzazioni per verificare il ruolo
        List<Organization> organizations = organizationDAO.findAll();

        for (Organization organization : organizations) {
            if (organization.getOwners().contains(user)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));
            }
            if (organization.getMembers().contains(user)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
        }

        if (user.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        // Se nessun ruolo trovato, default a USER
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorities;
    }
}
