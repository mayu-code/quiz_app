package com.app.quiz_app.jwt;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.quiz_app.entities.Admin;
import com.app.quiz_app.repository.AdminRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = this.repository.findByEmail(username).get();

        if (admin == null) {
            throw new UsernameNotFoundException("Invalid Credenitials !");
        }

        return User.builder().username(admin.getEmail()).password(admin.getPassword()).roles(admin.getRole())
                .authorities(admin.getAuthorities()).build();
    }

}
