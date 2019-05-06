package com.krm.security;

import com.krm.contants.AccountStatus;
import com.krm.contants.ResponseMessage;
import com.krm.exception.OneException;
import com.krm.form.CreateAccountForm;
import com.krm.form.LoginForm;
import com.krm.model.User;
import com.krm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static com.krm.contants.Constants.ACCESS_TOKEN;
import static com.krm.contants.ResponseMessage.LOGIN_SUCCESSFUL;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    public ResponseEntity signup(CreateAccountForm form) {

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            throw new OneException("password", ResponseMessage.CONFIRM_PASSWORD_DOES_NOT_MATCH);
        }

        if (userRepository.findByUsername(form.getUsername()) != null) {
            throw new OneException("username", ResponseMessage.USERNAME_TAKEN);
        }

        User user = new User();
        user.setCreatedAt(new Date());
        user.setUpdateAt(new Date());

        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setUsername(form.getUsername());

        user.setStatus(AccountStatus.ACTIVATED);
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(ResponseMessage.ACCOUNT_CREATED);
    }

    public ResponseEntity signin(LoginForm loginForm) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        HttpCookie cookie = ResponseCookie.from(ACCESS_TOKEN, jwt).path("/").build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LOGIN_SUCCESSFUL);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username " + username);
        }

        return user;
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public Optional<User> loadUserById(String id) {
        return userRepository.findById(id);
    }

}
