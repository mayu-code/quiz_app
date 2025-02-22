package com.app.quiz_app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quiz_app.dto.request.LoginRequest;
import com.app.quiz_app.dto.request.RegisterRequest;
import com.app.quiz_app.dto.response.DataResponse;
import com.app.quiz_app.dto.response.JwtResponse;
import com.app.quiz_app.dto.response.MessageResponse;
import com.app.quiz_app.entities.Admin;
import com.app.quiz_app.jwt.CustomUserDetailsService;
import com.app.quiz_app.jwt.JwtProvider;
import com.app.quiz_app.repository.AdminRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://quizbyayush.netlify.app")
public class AuthController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {

        MessageResponse res = new MessageResponse();

        Optional<Admin> isExits = this.adminRepository.findByEmail(request.getEmail());

        if (isExits.isPresent()) {
            res.setMessage("Admin Already Exits ");
            res.setStatus(HttpStatus.BAD_REQUEST);
            res.setStatusCode(400);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        try {

            Admin admin = new Admin();
            admin.setEmail(request.getEmail());
            admin.setRole("ADMIN");
            admin.setPassword(passwordEncoder().encode(request.getPassword()));

            this.adminRepository.save(admin);

            res.setMessage("Admin Added ");
            res.setStatus(HttpStatus.OK);
            res.setStatusCode(200);

            return ResponseEntity.status(HttpStatus.OK).body(res);

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setStatusCode(500);
            res.setMessage("Internal Server Error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);

        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest loginRequest) {
        JwtResponse res = new JwtResponse();

        Optional<Admin> admin = this.adminRepository.findByEmail(loginRequest.getEmail());

        if (!admin.isPresent()) {
            res.setStatus(HttpStatus.UNAUTHORIZED);
            res.setMessage("Invalid email or password");
            res.setStatusCode(401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        UserDetails adminDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        // Validate the password
        boolean isPasswordValid = passwordEncoder().matches(
                loginRequest.getPassword(),
                adminDetails.getPassword());

        if (!isPasswordValid) {
            res.setStatus(HttpStatus.FORBIDDEN);
            res.setMessage("Invalid email or password");
            res.setStatusCode(403);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
        }

        // Authenticate the admin
        Authentication authentication = adminAuthenticate(adminDetails.getUsername(), loginRequest.getPassword());

        // Generate JWT token
        String token = JwtProvider.generateToken(authentication);

        this.adminRepository.save(admin.get());

        // Prepare response
        res.setToken(token);
        res.setStatusCode(200);
        res.setStatus(HttpStatus.OK);
        res.setMessage("Admin login successful");

        return ResponseEntity.of(Optional.of(res));
    }

    private Authentication adminAuthenticate(String email, String password) {
        UserDetails details = userDetailsService.loadUserByUsername(email);
        if (details == null) {
            throw new BadCredentialsException("invalid Credintails");
        }

        return new UsernamePasswordAuthenticationToken(details, password, details.getAuthorities());

    }
}
