package com.app.quiz_app.service;

import java.util.Optional;

import com.app.quiz_app.entities.Admin;

public interface AdminService {
    Optional<Admin> getAdminByJwt(String jwt);
}
