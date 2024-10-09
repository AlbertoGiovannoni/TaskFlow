package com.unifi.taskflow.businessLogic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.User;

import org.springframework.security.core.GrantedAuthority;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Ottieni le autorità (ruoli) per l'utente
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Crea e ritorna l'oggetto UserDetails con i ruoli appropriati
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getUserInfo().getPassword(), authorities);
    }
}