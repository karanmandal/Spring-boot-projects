package com.krm.service;

import com.krm.model.User;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseService {

    public Optional<User> getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof User) {
            return Optional.of((User) principal);
        }

        return Optional.empty();
    }
}
