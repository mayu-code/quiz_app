package com.app.quiz_app.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.quiz_app.entities.Admin;
import com.app.quiz_app.jwt.JwtProvider;
import com.app.quiz_app.repository.AdminRepository;
import com.app.quiz_app.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Optional<Admin> getAdminByJwt(String jwt) {
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        return this.adminRepository.findByEmail(email);
    }

}
