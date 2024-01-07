package com.dpap.bookingapp.users.web;

import com.dpap.bookingapp.auth.AuthenticationService;
import com.dpap.bookingapp.users.User;
import com.dpap.bookingapp.users.UserDatabase;
import com.dpap.bookingapp.users.exception.UserNotFoundException;
import com.dpap.bookingapp.users.UserService;
import com.dpap.bookingapp.users.dto.EmailRequest;
import com.dpap.bookingapp.users.dto.PasswordRequest;
import com.dpap.bookingapp.users.dto.RegisterUserRequest;
import com.dpap.bookingapp.users.dto.UserQueryDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:4200")
@Tag(name = "Users")
class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationUserService;
    private final UserDatabase userDatabase;

    public UserController(UserService userService, AuthenticationService authenticationUserService, UserDatabase userDatabase) {
        this.userService = userService;
        this.authenticationUserService = authenticationUserService;
        this.userDatabase = userDatabase;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserQueryDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me/info")
    ResponseEntity<UserDetailsDTO> getInfoAboutMe() {
        var user = authenticationUserService.getLoggedUser();
        return ResponseEntity.ok(userService.findDetailsByUsername(user.username()));

    }


    @GetMapping("/{username}/info")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<UserQueryDto> getInfoAboutUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username)));
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userDatabase.findById(id).orElseThrow(() -> new RuntimeException("")));
    }


    @PutMapping("/password")
    ResponseEntity<?> changePasswordByLoggedUser(@Valid @RequestBody PasswordRequest passwordRequest) {
        userService.changePassword(passwordRequest, authenticationUserService.getLoggedUser());
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/email")
    ResponseEntity<?> changeEmailByLoggedUser(@Valid @RequestBody EmailRequest emailRequest) {
        userService.changeEmail(emailRequest, authenticationUserService.getLoggedUser());
        return ResponseEntity.status(204).build();
    }

}