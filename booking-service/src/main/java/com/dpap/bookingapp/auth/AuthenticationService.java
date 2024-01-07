package com.dpap.bookingapp.auth;

import com.dpap.bookingapp.auth.dto.AuthenticationRequest;
import com.dpap.bookingapp.auth.dto.AuthenticationResponse;
import com.dpap.bookingapp.auth.dto.RegisterRequest;
import com.dpap.bookingapp.auth.userdetails.UserDetailsImpl;
import com.dpap.bookingapp.users.*;
import com.dpap.bookingapp.users.dto.UserQueryDto;
import com.dpap.bookingapp.users.exception.UserNotFoundException;
import com.dpap.bookingapp.users.roles.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserDatabase userDatabase;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(PasswordEncoder passwordEncoder, JWTService jwtService, UserDatabase userDatabase, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDatabase = userDatabase;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = new User();
        validate(registerRequest);
        user.setEmail(registerRequest.email());
        user.setUsername(registerRequest.email());
        user.setFirstname(registerRequest.firstname());
        user.setLastname(registerRequest.lastname());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.addRole(Role.USER);

        userDatabase.save(user);
        return new AuthenticationResponse(
                jwtService.generateToken(UserDetailsImpl.build(user)));
    }

    private void validate(RegisterRequest registerRequest) {
        if(!checkEmailUniqueness(registerRequest.email())){
            throw new RuntimeException("Account with email: " + registerRequest.email() + " already exists.");
        }
    }

    public boolean checkEmailUniqueness(String email) {
        return !userDatabase.existsByEmail(email);
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );
        var user = userDatabase.findByUsername(authenticationRequest.username());
        return new AuthenticationResponse(
                jwtService.generateToken(UserDetailsImpl.build(user.orElseThrow(() -> new UsernameNotFoundException(authenticationRequest.username())))));
    }

    public UserQueryDto getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        var user = userDatabase.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return new UserQueryDto(user.getId(), user.getUsername());
    }
}
