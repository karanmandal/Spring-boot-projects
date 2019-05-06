package com.krm.security;

import com.krm.form.CreateAccountForm;
import com.krm.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private AuthService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @ModelAttribute CreateAccountForm createAccountForm) {
        return userService.signup(createAccountForm);
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@Valid @ModelAttribute LoginForm loginForm) {
        return userService.signin(loginForm);
    }

}
