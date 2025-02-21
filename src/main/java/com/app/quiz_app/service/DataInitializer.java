package com.app.quiz_app.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.quiz_app.entities.Admin;
import com.app.quiz_app.repository.AdminRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AdminRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("aniket@gmail.com").isEmpty()) {
            Admin admin = new Admin();
            admin.setEmail("aniket@gmail.com");
            admin.setPassword(passwordEncoder.encode("1458"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Admin user created: username = aniket@gmail.com, password = 1458");
        }
    }
}
