package com.krm.service;

import com.krm.model.User;
import com.krm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity currentUser() {
        return ResponseEntity.ok(getCurrentUser().get());
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
