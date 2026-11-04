package com.ecommerce.store_backend.controller;

import com.ecommerce.store_backend.dto.UserRequestDTO;
import com.ecommerce.store_backend.model.User;
import com.ecommerce.store_backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @Autowired
    private com.ecommerce.store_backend.security.JwtUtils jwtUtils;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
   
   

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    // 1. Get all users
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }

    // 2. Get user by ID
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword())); 
        user.setRole("CUSTOMER");

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    
    //Login User Mapping
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody com.ecommerce.store_backend.dto.LoginRequestDTO request) {
        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(request.getUsername());

        return ResponseEntity.ok(java.util.Collections.singletonMap("token", jwt));
    }
}
