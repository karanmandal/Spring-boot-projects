package com.krm.controller;

import com.krm.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/signup")
    public String createAccountView() {
        return "signup.html";
    }

    @GetMapping("/signin")
    public String loginView() {

        // Check if user is already logged in or not
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated() && auth.getPrincipal() instanceof User) {

            System.out.println("LOGGED IN");

            return "redirect:/";
        }

        return "signin.html";
    }

    @GetMapping("/chatpanel")
    public String chatPanelView() {
        return "index.html";
    }
}
