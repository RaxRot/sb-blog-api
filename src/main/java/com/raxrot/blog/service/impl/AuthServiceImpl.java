package com.raxrot.blog.service.impl;

import com.raxrot.blog.dto.LoginDTO;
import com.raxrot.blog.dto.RegisterDTO;
import com.raxrot.blog.entity.Role;
import com.raxrot.blog.entity.User;
import com.raxrot.blog.exception.BlogAPIException;
import com.raxrot.blog.repository.RoleRepository;
import com.raxrot.blog.repository.UserRepository;
import com.raxrot.blog.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(LoginDTO loginDTO) {
        log.info("Login attempt for: {}", loginDTO.getUsernameOrEmail());

       Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return  "User login successful";
    }

    @Override
    public String register(RegisterDTO registerDTO) {
        log.info("Register attempt for: {}", registerDTO.getUsername());

        //add check for username exist in DB
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BlogAPIException("Username is already exist!");
        }
        //add check for email exist in DB
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BlogAPIException("Email is already in use!");
        }

        User user=new User();
        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        Set<Role> roles=new HashSet<>();
        Role userRole=roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        log.info("User registered successfully: {}", user.getUsername());

        return "User registered successfully";
    }
}
