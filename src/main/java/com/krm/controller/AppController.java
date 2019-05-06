package com.krm.controller;

import com.krm.service.UserReactiveService;
import com.krm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserReactiveService userReactiveService;

    @GetMapping("/me")
    public ResponseEntity findUser() {
        return userService.currentUser();
    }

//    @GetMapping(value = "/stream")
//    public Flux<ResponseEntity> stream() {
//        return userReactiveService.getEventStream().map(ResponseEntity::ok);
//    }
}
